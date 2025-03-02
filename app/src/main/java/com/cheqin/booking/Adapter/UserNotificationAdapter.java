package com.cheqin.booking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.Bean.UserNotificationBean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.VolleySingleton;

import java.util.List;


public class UserNotificationAdapter extends ArrayAdapter<UserNotificationBean> {

    Context context;
    private List<UserNotificationBean> bean = null;
    LayoutInflater mInflater;
    String mimeType = "text/html";
    String encoding = "utf-8";


    public UserNotificationAdapter(Context context, List<UserNotificationBean> rowItems1) {
        // TODO Auto-generated constructor stub
        super(context, R.layout.user_notification_item, rowItems1);
        this.context = context;
        bean = rowItems1;
    }

    private class ViewHolder {

        TextView msge;
        NetworkImageView image;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

         mInflater = (LayoutInflater) this.context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_notification_item, null);
            holder = new ViewHolder();
            holder.msge = (TextView) convertView.findViewById(R.id.webView);
            holder.image = (NetworkImageView) convertView.findViewById(R.id.mydeals);
//            holder.msge.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,"idsohifi",Toast.LENGTH_LONG).show();
//                }
//            });
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();
//            holder.msge.loadData(bean.get(position).getmsge(),mimeType,encoding );
        holder.msge.setText(bean.get(position).getmsge().substring(3));
        holder.image.setImageUrl("http://www.mydeals247.com/HomeV2.5/image/logo1.png", VolleySingleton.getInstance(context).getImageLoader());
            return convertView;

    }
}