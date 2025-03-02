package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cheqin.booking.Bean.Hoteltypebean;
import com.cheqin.booking.Bean.RoomTypeBean;
import com.cheqin.booking.R;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeDataAdapter extends ArrayAdapter<RoomTypeBean>{
    Activity context;
    private List<RoomTypeBean> bean = null;
    public  ArrayList<String> check;
    public RoomTypeDataAdapter(Activity context, List<RoomTypeBean> roomTypeBeans) {
        super(context, R.layout.list_single_check,roomTypeBeans);
        this.context = context;
        check = new ArrayList<>();
        this.bean  = roomTypeBeans;
    }
    private class ViewHolder {
        TextView id;
        TextView name;
        RadioButton rb;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_single_check, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_MainText);
//            holder.rb = (RadioButton) convertView.findViewById(R.id.rb_Choice);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

            holder.name.setText(bean.get(position).getName());
//            Log.e("id",UserNotificationBean.get(position).getId().toString());
//            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked){
//                        check.add(UserNotificationBean.get(position).getId());
//                        Log.e("checked",UserNotificationBean.get(position).getId().toString());
//                    }else{
//                        check.remove(UserNotificationBean.get(position).getId());
//                        Log.e("remove",UserNotificationBean.get(position).getId());
//                        notifyDataSetChanged();
//                    }
////                    int getPosition = (int) buttonView.getTag();
////                    UserNotificationBean.get(getPosition).setId(buttonView.isChecked());
//                }
//            });
        return convertView;

    }

}
