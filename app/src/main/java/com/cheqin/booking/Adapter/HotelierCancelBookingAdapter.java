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

/**
 * Created by Suhas on 10-Mar-16.
 */
public class HotelierCancelBookingAdapter extends ArrayAdapter<Confirm_Bean> {
        Context context;
private List<Confirm_Bean> confirm_bean = null;
        LayoutInflater mInflater;

public HotelierCancelBookingAdapter(Context context, List<Confirm_Bean> rowitems) {
        super(context, R.layout.hotel_cancel_booking_items, rowitems);
        this.context = context;
        confirm_bean = rowitems;
        }

private class ViewHolder {
    TextView txt_type;
    TextView txt_price;
    Button btn;
}

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        mInflater = (LayoutInflater) this.context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.hotel_cancel_booking_items, null);
            holder.txt_price = (TextView) convertView.findViewById(R.id.hotel_canceled_hotel_price);
            holder.txt_type = (TextView) convertView.findViewById(R.id.hotel_canceled_hotel_type);
//            holder.btn = (Button) convertView.findViewById(R.id.button5);
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

        holder.txt_type.setText(confirm_bean.get(position).getHotel_class());
        holder.txt_price.setText(confirm_bean.get(position).getCurrency()+" "+confirm_bean.get(position).getHotel_price());
        return convertView;
    }
}
