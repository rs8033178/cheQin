package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cheqin.booking.Bean.HotelierOfferDetailsBean;
import com.cheqin.booking.R;

import java.text.DecimalFormat;
import java.util.List;

public class HotelierShowDetailsAdapter extends ArrayAdapter<HotelierOfferDetailsBean> {
    Activity context;
    private List<HotelierOfferDetailsBean> bean_tab = null;
    DecimalFormat commaformatter;

    public HotelierShowDetailsAdapter(Activity context, List<HotelierOfferDetailsBean> rowItems, DecimalFormat commaformater) {
                    super(context, R.layout.hotelier_show_posted_offers_items, rowItems);
        this.context = context;
        bean_tab = rowItems;
        commaformatter = commaformater;
    }
    private class ViewHolder {
        TextView txt_hotel_details;
        TextView txt_hotel_locality;
        TextView txt_user_price;
        TextView txt_your_rank;
        TextView txt_your_price;
        TextView button_view_other_offers;
        TextView button_update_offer;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView=mInflater.inflate(R.layout.hotelier_show_posted_offers_items, null);
            holder = new ViewHolder();
            holder.txt_hotel_details = (TextView) convertView.findViewById(R.id.posted_hotel_details);
            holder.txt_hotel_locality = (TextView) convertView.findViewById(R.id.posted_hotel_locality);
            holder.txt_user_price = (TextView) convertView.findViewById(R.id.posted_user_price);
            holder.txt_your_rank = (TextView) convertView.findViewById(R.id.posted_hotel_rank);
            holder.txt_your_price = (TextView) convertView.findViewById(R.id.posted_your_price);
            holder.button_view_other_offers = (TextView) convertView.findViewById(R.id.button_view_other_offers);
            holder.button_update_offer = (TextView) convertView.findViewById(R.id.button_update_offer);


            convertView.setTag(holder);


        } else
            holder = (ViewHolder) convertView.getTag();


            holder.txt_hotel_details.setText(bean_tab.get(position).getHotel_info());
            holder.txt_hotel_locality.setText(bean_tab.get(position).getLocality());
            holder.txt_user_price.setText(bean_tab.get(position).getCurrency()+" " + bean_tab.get(position).getUser_price());
            holder.txt_your_rank.setText(bean_tab.get(position).getMy_offer_rank());
            holder.txt_your_price.setText(bean_tab.get(position).getCurrency()+" " +commaformatter.format(Integer.valueOf(bean_tab.get(position).getYour_price())));
//            holder.txt_min_price.setText(bean_tab.get(position).getmin_price());
//            holder.txt_sub_category_name.setText(bean_tab.get(position).getsub_category_name());

        holder.button_update_offer.setTag(position);
        holder.button_view_other_offers.setTag(position);

        if (Integer.valueOf(bean_tab.get(position).getLengt())>1){
            holder.button_view_other_offers.setVisibility(View.VISIBLE);
            holder.button_update_offer.setText("Compete");
            holder.button_view_other_offers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos1 = (Integer)v.getTag();
                  //  Intent intent = new Intent(context, HotelierViewOtherOffers.class);
                    Bundle b = new Bundle();
                    b.putString("uid", bean_tab.get(pos1).getUser_request_id());
                    b.putString("currency",bean_tab.get(pos1).getCurrency());
//                    String uid =bean_tab.get(position).getUser_request_id();
//                            Log.e("uid",uid);
                    //intent.putExtras(b);
                    //context.startActivity(intent);
                }
            });
        }else if(Integer.valueOf(bean_tab.get(position).getLengt())<=1){
            holder.button_view_other_offers.setVisibility(View.GONE);
            holder.button_update_offer.setText("Update Offer");
        }


        holder.button_update_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer)v.getTag();

//                Intent intent1 = new Intent(context, HotelierUpdateOffer.class);
                Bundle b1 = new Bundle();
                b1.putString("user_req_id", bean_tab.get(pos).getUser_request_id());
                b1.putString("moid", bean_tab.get(pos).getMoid());
                b1.putString("price", bean_tab.get(pos).getYour_price());
                b1.putString("minprice", bean_tab.get(pos).getMin_price());
                b1.putString("mintime", bean_tab.get(pos).getMin_time());
                b1.putString("currency",bean_tab.get(pos).getCurrency());
                b1.putString("item_info",bean_tab.get(pos).getHotel_info());
                b1.putString("check_in_date",bean_tab.get(pos).getCheck_in_date());
                b1.putString("check_out_date",bean_tab.get(pos).getCheck_out_date());
                b1.putString("rooms",bean_tab.get(pos).getRooms());
                b1.putString("childrens",bean_tab.get(pos).getChildrens());
                b1.putString("adults",bean_tab.get(pos).getAdults());
                b1.putString("user_price",bean_tab.get(pos).getUser_price());
                b1.putString("min_offer_price",bean_tab.get(pos).getLowest_offer());
              //  intent1.putExtras(b1);
                //context.startActivity(intent1);
                context.finish();
            }
        });


            return convertView;
        }



}
