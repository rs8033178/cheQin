package com.cheqin.booking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cheqin.booking.Bean.Confirm_Bean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.Common;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserCancelBookingAdapter extends ArrayAdapter<Confirm_Bean> {
    private Context context;
    private List<Confirm_Bean> confirm_bean = null;
    private LayoutInflater mInflater;

    public UserCancelBookingAdapter(Context context, List<Confirm_Bean> rowitems) {
        super(context, R.layout.user_cancel_booking_items, rowitems);
        this.context = context;
        confirm_bean = rowitems;
    }

    private class ViewHolder {
        TextView txt_type;
        TextView txt_price;
        TextView txt_address;
        TextView txt_room_type;
        TextView txt_hotel_name;
        Button btn;
    }

    @NotNull
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        ViewHolder holder = null;

        mInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_cancel_booking_items, null);
            holder.txt_price = convertView.findViewById(R.id.canceled_hotel_price);
            holder.txt_type = convertView.findViewById(R.id.canceled_hotel_type);
            holder.txt_address = convertView.findViewById(R.id.canceled_hotel_address);
            holder.txt_room_type = convertView.findViewById(R.id.canceled_hotel_room_type);
            holder.txt_hotel_name = convertView.findViewById(R.id.canceled_hotel_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_type.setText(confirm_bean.get(position).getHotel_class());
        holder.txt_hotel_name.setText(confirm_bean.get(position).getHotel_name());
        holder.txt_address.setText(confirm_bean.get(position).getAddress());
        holder.txt_room_type.setText("Room Type : " + confirm_bean.get(position).getOffered_room_type());
        String price = confirm_bean.get(position).getHotel_price();
        if (price != null && price.contains(".")) {
            holder.txt_price.setText("Bargained Offer : " + Common.getCurrencySymbol(confirm_bean.get(position).getCurrency()) + "" + +(int) Math.floor(Double.parseDouble(price)));
        } else {
            holder.txt_price.setText("Bargained Offer : " + Common.getCurrencySymbol(confirm_bean.get(position).getCurrency()) + "" + price);
        }


        return convertView;
    }
}
