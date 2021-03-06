package vicinity.vicinity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vicinity.model.VicinityMessage;

/**
 * An adapter that takes a list of VicinityMessage objects
 * and binds it to a ListView,  each list item
 * representing a VicinityMessage object
 */
public class ChatAdapter extends ArrayAdapter<VicinityMessage> {

    private List<VicinityMessage> vicinityMessages;
    private LayoutInflater mInflater;
    private Context ctx;

    /**
     * Public constructor
     * @param context Context
     * @param resource int
     */
    public ChatAdapter(Context context, int resource) {
        super(context, resource);
        vicinityMessages = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        ctx = context;

    }

                    /*----------Overridden Methods------------*/

    @Override
    public void add(VicinityMessage object) {
        vicinityMessages.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return vicinityMessages.size();
    }

    @Override
    public VicinityMessage getItem(int position) {
        return vicinityMessages.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.chat_box_layout, null);

            holder = new ViewHolder();
            holder.chat_text = (TextView) convertView.findViewById(R.id.chatText);
            holder.name_text = (TextView) convertView.findViewById(R.id.nameOfFriend);
            holder.chat_image = (ImageView) convertView.findViewById(R.id.chatImage);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Changes the position of the message based on whether
        // it is sent or received and adds a margin to it.
        // Sent messages are placed on the right and received
        // messages are placed on the left side of the screen.
        if(!vicinityMessages.get(position).isMyMsg()){
            params.gravity = Gravity.LEFT;
            params.rightMargin = 120;
        }
        else {
            params.gravity = Gravity.RIGHT;
            params.leftMargin = 120;
        }

        // ImageView and message TextView are initially invisible
        holder.chat_text.setVisibility(View.GONE);
        holder.chat_image.setVisibility(View.GONE);

        // If the message body is not empty shows the message's
        // TextView and set the text to it
        if(!vicinityMessages.get(position).getMessageBody().equals("")){
            holder.chat_text.setVisibility(View.VISIBLE);
            holder.chat_text.setText(vicinityMessages.get(position).getMessageBody());
            holder.chat_text.setBackgroundDrawable(vicinityMessages.get(position).isMyMsg() ?
                    ctx.getResources().getDrawable(R.drawable.chatboxright) : ctx.getResources().getDrawable(R.drawable.chatboxleft));
            holder.name_text.setText(vicinityMessages.get(position).isMyMsg() ? "" : vicinityMessages.get(position).getFriendName());
            holder.chat_text.setLayoutParams(params);

        }
        // If the message body is empty then it contains a photo
        // makes the ImageView visible and sets the photo.
        else {
            holder.chat_image.setVisibility(View.VISIBLE);
            String imageBitmap = vicinityMessages.get(position).getImageString();
            byte[] decodedString = Base64.decode(imageBitmap, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.chat_image.setImageBitmap(decodedByte);
            holder.chat_image.setBackgroundDrawable(vicinityMessages.get(position).isMyMsg() ?
                    ctx.getResources().getDrawable(R.drawable.chatboxright) : ctx.getResources().getDrawable(R.drawable.chatboxleft));
            holder.name_text.setText(vicinityMessages.get(position).isMyMsg() ? "" : vicinityMessages.get(position).getFriendName());
            holder.chat_image.setLayoutParams(params);

        }

        return convertView;
    }

    static class ViewHolder{
        TextView chat_text, name_text;
        ImageView chat_image;
    }
}
