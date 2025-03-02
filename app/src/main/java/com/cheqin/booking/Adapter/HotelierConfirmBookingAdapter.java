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

import java.util.List;


public class HotelierConfirmBookingAdapter extends ArrayAdapter<Confirm_Bean> {
    Context context;
    private List<Confirm_Bean> hotelierConfirm_bean = null;
    LayoutInflater mInflater;

    public HotelierConfirmBookingAdapter(Context context, List<Confirm_Bean> rowitems) {
        super(context, R.layout.hotelier_confirm_booking_items, rowitems);
        this.context = context;
        hotelierConfirm_bean = rowitems;
    }

    private class ViewHolder {
        TextView txt_type;
        TextView txt_price;
        TextView btn;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

         mInflater = (LayoutInflater) this.context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.hotelier_confirm_booking_items, null);
            holder.txt_price = (TextView) convertView.findViewById(R.id.confirm_hotel_hotel_price);
            holder.txt_type = (TextView) convertView.findViewById(R.id.confirm_hotel_hotel_type);
            holder.btn = (TextView) convertView.findViewById(R.id.button5);
//            holder.btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "dfdfgbfd", Toast.LENGTH_LONG).show();
//                }
//            });
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_type.setText("Purchased by "+hotelierConfirm_bean.get(position).getPurchase()+" ("+hotelierConfirm_bean.get(position).getHotel_class()+")");
        holder.txt_price.setText(hotelierConfirm_bean.get(position).getCurrency()+" " + hotelierConfirm_bean.get(position).getHotel_price());
        holder.btn.setText(hotelierConfirm_bean.get(position).getDate().substring(0,10));
        return convertView;
    }
}
