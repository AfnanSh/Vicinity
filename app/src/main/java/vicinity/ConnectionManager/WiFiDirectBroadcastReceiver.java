
package vicinity.ConnectionManager;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.util.Log;
import vicinity.model.Globals;

/**
 * this class is a WiFi BroadcastReceiver
 * Listens to wifi events and alerts the system
 * this class will be moved as it is to Vicinity
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    final String TAG ="WiFiBCReceiver";
    private WifiP2pManager manager;
    private Channel channel;
    private Context context;

    /**
     * @param manager WifiP2pManager system service
     * @param channel Wifi p2p channel
     * @param context activity associated with the receiver
     */
    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
                                       Context context) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.context = context;
    }

    /**
     * WiFi events happen here
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG,"WiFi BC onReceive" );


        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //Broadcast intent action indicating that peer discovery has either started or stopped.
            if (manager == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                Globals.isConnectedToANetwork=true;
                // we are connected with the other device, request connection
                // info to find group owner IP
                Log.i(TAG,"Connected to p2p network. Requesting network details...");
                Log.i(TAG,"Network info: "+Globals.isConnectedToANetwork);
                manager.requestConnectionInfo(channel,(ConnectionInfoListener) context);
            } else {
                Globals.isConnectedToANetwork=false;
                Log.d(TAG,"NOT Connected to P2P network!");

            }
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
                .equals(action)) {
            WifiP2pDevice device = (WifiP2pDevice) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Log.d(TAG, "Device name: "+device.deviceName+" Device status -" + device.status);

        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.i(TAG,"WIFI_P2P_PEERS_CHANGED_ACTION");
            // The peer list has changed!
            //update devices list here


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.i(TAG,"WIFI_P2P_CONNECTION_CHANGED_ACTION");
            // Connection state changed!



        }
    }
}
