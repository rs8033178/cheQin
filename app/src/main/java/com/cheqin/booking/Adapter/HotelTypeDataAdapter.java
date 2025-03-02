package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cheqin.booking.Bean.Hoteltypebean;
import com.cheqin.booking.R;

import java.util.ArrayList;
import java.util.List;

public class HotelTypeDataAdapter extends ArrayAdapter<Hoteltypebean> {
    Activity context;
    private List<Hoteltypebean> bean = null;
    public ArrayList<String> check;

    public HotelTypeDataAdapter(Activity context, List<Hoteltypebean> hotelcategories) {
        super(context, R.layout.list_single_check, hotelcategories);
        this.context = context;
        check = new ArrayList<>();
        this.bean = hotelcategories;
    }

    private class ViewHolder {
        TextView id;
        TextView name;
        RadioButton rb;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_single_check, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_MainText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(bean.get(position).getName());
        return convertView;

    }

}
