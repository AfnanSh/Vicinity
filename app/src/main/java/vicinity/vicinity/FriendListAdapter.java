package vicinity.vicinity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.BaseAdapter;


import java.util.ArrayList;

import vicinity.ConnectionManager.WiFiP2pService;
import vicinity.model.Friend;


public class FriendListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    ArrayList<WiFiP2pService> services;

    public FriendListAdapter(Context context, ArrayList<WiFiP2pService> services){
        this.services = services;
        mInflater = LayoutInflater.from(context);

    }

    public void setServices(ArrayList<WiFiP2pService> s){
        for (int i = 0; i < s.size(); i++) {
            services.add(s.get(i));
        }
    }


    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.friend_row_view, null);
            holder = new ViewHolder();
            holder.textName = (TextView) convertView.findViewById(R.id.friend_row);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textName.setText(services.get(position).getInstanceName());


        return convertView;
    }

    static class ViewHolder{
        TextView textName;
    }
}
