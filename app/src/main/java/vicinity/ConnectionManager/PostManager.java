package vicinity.ConnectionManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

import vicinity.model.Comment;
import vicinity.model.Globals;
import vicinity.model.Post;

/**
 * This class extends AsyncTask and performs broadcasting a post operation in the
 * background, this was implemented as a solution for NetworkOnMainThreadException
 * that occurs when performing network operations on main thread.
 */
public class PostManager extends AsyncTask <Void, Void, Void> {

    //TODO this class shall be named UDPBroadcastManager

    private static final String TAG = "PostManager";
    private Post post;
    private Comment comment;
    //private boolean flag;//to determine if it's a comment or a post
    private static final int TIMEOUT_MS = 500;
    DatagramSocket socket;

    private boolean requestFlag = false;//request
    private boolean commentFlag = false;
    private boolean postFlag = false;
    InetAddress requestIP;



    /**
     * Set requestIP from ConnectAndDiscoverService
     * to send a friend request to a peer
     * @param requestIP IP address of a peer
     */
    public void setRequest(InetAddress requestIP){
        this.requestIP = requestIP;
        requestFlag=true;
    }

    /**
     * Set post from the NewPost class
     * @param post a new post to be broadcasted
     */
    public void setPost(Post post){
        this.post=post;
        postFlag = true;

    }

    /**
     * Sets comment from addComment class
     * @param comment a new comment to be broadcasted
     */
    public void setComment(Comment comment){this.comment = comment;
        commentFlag=true;}


    @Override
    protected void onPreExecute(){


    }

    @Override
    protected Void doInBackground(Void... param){
        Log.i(TAG,"doInBackground() -> Post: "+post);

        try{

            socket = new DatagramSocket(null);
            SocketAddress socketAddr = new InetSocketAddress(Globals.SERVER_PORT);
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
            socket.bind(socketAddr);
            socket.setSoTimeout(TIMEOUT_MS);

            if(requestFlag){
                sendFriendRequest(requestIP);
            }
            else if(postFlag) {
                Log.i(TAG,"inside if, flag= "+postFlag);
                sendPost(post, socket);
            }
            else if (commentFlag){
                Log.i(TAG,"inside else, flag= "+commentFlag);
                sendComment(comment, socket);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute (Void result){

    }

    /**
     * Broadcasts a new post to the broadcast address 192.168.49.255
     * @param post a new post to be broadcasted
     * @param socket DatagramSocket
     */
    public void sendPost(Post post, DatagramSocket socket){
        try{
            socket.setBroadcast(true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(post);
            byte [] data = outputStream.toByteArray();
            InetAddress broadcastIP = InetAddress.getByName("192.168.49.255");
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length,
                    broadcastIP, Globals.SERVER_PORT);
            socket.send(datagramPacket);
            String senderIP = datagramPacket.getAddress().getHostAddress();
            Log.d(TAG, "Sending post: "+post+" senderIP: "+senderIP);

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Broadacasts a comment of a specified post
     * @param comment Comment object
     * @param socket DatagramSocket
     */
    public void sendComment(Comment comment, DatagramSocket socket){//-Lama
        try{

            Log.i(TAG,"Sending comment: "+comment);
            socket.setBroadcast(true);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(comment);
            byte [] data = outputStream.toByteArray();

            Log.i(TAG,"socket.getInetAddress();: "+socket.getInetAddress());
            InetAddress broadcastIP = InetAddress.getByName("192.168.49.255");
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length,
                    broadcastIP, Globals.SERVER_PORT);
            socket.send(datagramPacket);
            String senderIP = datagramPacket.getAddress().getHostAddress();
            Log.d(TAG, " senderIP: "+senderIP);

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Broadcast (MAC,IP) Addresses to peers in local network
     * @param addresses a HashMap containing addresses from group owner
     */
    public void sendAdresses(HashMap addresses){
        try{
            if(!addresses.isEmpty()) {
                Log.i(TAG, "Sending addresses");
                socket.setBroadcast(true);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(addresses);
                byte[] data = outputStream.toByteArray();

                Log.i(TAG, "socket.getInetAddress();: " + socket.getInetAddress());
                InetAddress broadcastIP = InetAddress.getByName("192.168.49.255");
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length,
                        broadcastIP, Globals.SERVER_PORT);
                socket.send(datagramPacket);

            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Opens a socket directly to a request server
     * at another peer to send a friend's request
     * @param requestedIP IP address of a peer
     */
    public static void sendFriendRequest(InetAddress requestedIP){
        try{
            Log.i("Request","Sending request to.."+requestedIP);
            Socket requestSocket = new Socket (requestedIP, Globals.REQUEST_PORT);
            requestSocket.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}



