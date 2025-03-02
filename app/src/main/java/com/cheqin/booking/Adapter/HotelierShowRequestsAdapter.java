package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheqin.booking.Bean.hotelier_show_requests_bean;
import com.cheqin.booking.R;

import java.util.List;

public class HotelierShowRequestsAdapter extends ArrayAdapter<hotelier_show_requests_bean> {
    Activity context;
    private List<hotelier_show_requests_bean> hotelier_show_requests_bean = null;


    public HotelierShowRequestsAdapter(Activity context, List<hotelier_show_requests_bean> row_Items1) {
        // TODO Auto-generated constructor stub
        super(context, R.layout.hotelier_show_req_items, row_Items1);
        this.context = context;
        hotelier_show_requests_bean = row_Items1;
    }

    /*private view holder class*/
    private class ViewHolder {

        TextView txt_checkinday;
        TextView txt_checkinmonth;
        TextView txt_checkinyear;
        TextView txt_checkoutday;
        TextView txt_checkoutmonth;
        TextView txt_checkoutyear;
        TextView txt_no_of_rooms;
        TextView txt_adult;
        TextView txt_childrens;
        TextView txt_price;
        TextView txt_item_info;
        TextView txt_city;
        TextView chkin_hh;
        TextView chkout_hh;
        TextView txt_user_distance;
        LinearLayout filter;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hotelier_show_req_items, null);
            holder = new ViewHolder();

            holder.txt_checkinday = (TextView) convertView.findViewById(R.id.hckin_date);
            holder.txt_checkinmonth = (TextView) convertView.findViewById(R.id.hckin_month);
            holder.txt_checkinyear = (TextView) convertView.findViewById(R.id.hckin_year);
            holder.txt_checkoutday = (TextView) convertView.findViewById(R.id.hckout_date);
            holder.txt_checkoutmonth = (TextView) convertView.findViewById(R.id.hckout_month);
            holder.txt_checkoutyear = (TextView) convertView.findViewById(R.id.hckout_year);
            holder.txt_no_of_rooms = (TextView) convertView.findViewById(R.id.user_req_rooms);
            holder.txt_adult = (TextView) convertView.findViewById(R.id.user_req_adults);
            holder.txt_childrens = (TextView) convertView.findViewById(R.id.user_req_childrens);
            holder.txt_city = (TextView) convertView.findViewById(R.id.user_req_city);
            holder.txt_price = (TextView) convertView.findViewById(R.id.user_req_price);
            holder.txt_item_info = (TextView) convertView.findViewById(R.id.user_req_iteminfo);
            holder.chkin_hh = (TextView) convertView.findViewById(R.id.chki_hh);
            holder.chkout_hh = (TextView) convertView.findViewById(R.id.chko_hh);

//            holder.txt_user_distance = (TextView) convertView.findViewById(R.id.user_distance);
//            holder.filter = (LinearLayout) convertView.findViewById(R.id.linear_filter);
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

//            String str_date_checkin= hotelier_show_requests_bean.get(position).getCheckin().substring(0,10);
//            String str_date_checkout= hotelier_show_requests_bean.get(position).getCheckout().substring(0,10);
//            holder.txt_checkinday.setText(str_date_checkin);
//            holder.txt_checkout.setText(str_date_checkout);
        if (hotelier_show_requests_bean.get(position).getShort_stay().equalsIgnoreCase("0")) {
            String daycheckin = hotelier_show_requests_bean.get(position).getCheckin().substring(8, 10);
            String monthcheckin = hotelier_show_requests_bean.get(position).getCheckin().substring(5, 7);
            String yearcheckin = hotelier_show_requests_bean.get(position).getCheckin().substring(0, 4);
            holder.txt_checkinyear.setText(yearcheckin);
            holder.txt_checkinday.setText(daycheckin);
            if (monthcheckin.equalsIgnoreCase("01")) {
                holder.txt_checkinmonth.setText("JAN");
            } else if (monthcheckin.equalsIgnoreCase("02")) {
                holder.txt_checkinmonth.setText("FEB");
            } else if (monthcheckin.equalsIgnoreCase("03")) {
                holder.txt_checkinmonth.setText("MAR");
            } else if (monthcheckin.equalsIgnoreCase("04")) {
                holder.txt_checkinmonth.setText("APR");
            } else if (monthcheckin.equalsIgnoreCase("05")) {
                holder.txt_checkinmonth.setText("MAY");
            } else if (monthcheckin.equalsIgnoreCase("06")) {
                holder.txt_checkinmonth.setText("JUN");
            } else if (monthcheckin.equalsIgnoreCase("07")) {
                holder.txt_checkinmonth.setText("JUL");
            } else if (monthcheckin.equalsIgnoreCase("08")) {
                holder.txt_checkinmonth.setText("AUG");
            } else if (monthcheckin.equalsIgnoreCase("09")) {
                holder.txt_checkinmonth.setText("SEP");
            } else if (monthcheckin.equalsIgnoreCase("10")) {
                holder.txt_checkinmonth.setText("OCT");
            } else if (monthcheckin.equalsIgnoreCase("11")) {
                holder.txt_checkinmonth.setText("NOV");
            } else if (monthcheckin.equalsIgnoreCase("12")) {
                holder.txt_checkinmonth.setText("DEC");
            }
            String daycheckout = hotelier_show_requests_bean.get(position).getCheckout().substring(8, 10);
            String monthcheckout = hotelier_show_requests_bean.get(position).getCheckout().substring(5, 7);
            String yearcheckout = hotelier_show_requests_bean.get(position).getCheckout().substring(0, 4);
            holder.txt_checkoutyear.setText(yearcheckout);
            holder.txt_checkoutday.setText(daycheckout);
            if (monthcheckout.equalsIgnoreCase("01")) {
                holder.txt_checkoutmonth.setText("JAN");
            } else if (monthcheckout.equalsIgnoreCase("02")) {
                holder.txt_checkoutmonth.setText("FEB");
            } else if (monthcheckout.equalsIgnoreCase("03")) {
                holder.txt_checkoutmonth.setText("MAR");
            } else if (monthcheckout.equalsIgnoreCase("04")) {
                holder.txt_checkoutmonth.setText("APR");
            } else if (monthcheckout.equalsIgnoreCase("05")) {
                holder.txt_checkoutmonth.setText("MAY");
            } else if (monthcheckout.equalsIgnoreCase("06")) {
                holder.txt_checkoutmonth.setText("JUN");
            } else if (monthcheckout.equalsIgnoreCase("07")) {
                holder.txt_checkoutmonth.setText("JUL");
            } else if (monthcheckout.equalsIgnoreCase("08")) {
                holder.txt_checkoutmonth.setText("AUG");
            } else if (monthcheckout.equalsIgnoreCase("09")) {
                holder.txt_checkoutmonth.setText("SEP");
            } else if (monthcheckout.equalsIgnoreCase("10")) {
                holder.txt_checkoutmonth.setText("OCT");
            } else if (monthcheckout.equalsIgnoreCase("11")) {
                holder.txt_checkoutmonth.setText("NOV");
            } else if (monthcheckout.equalsIgnoreCase("12")) {
                holder.txt_checkoutmonth.setText("DEC");
            }
        }else {
            holder.chkin_hh.setVisibility(View.VISIBLE);
            holder.chkout_hh.setVisibility(View.VISIBLE);
            String daycheckin = hotelier_show_requests_bean.get(position).getCheckin().substring(8, 10);
            String monthcheckin = hotelier_show_requests_bean.get(position).getCheckin().substring(5, 7);
            String yearcheckin = hotelier_show_requests_bean.get(position).getCheckin().substring(0, 4);
            holder.chkin_hh.setText(hotelier_show_requests_bean.get(position).getCheckin().substring(11,16));
            holder.txt_checkinyear.setText(yearcheckin);
            holder.txt_checkinday.setText(daycheckin);
            if (monthcheckin.equalsIgnoreCase("01")) {
                holder.txt_checkinmonth.setText("JAN");
            } else if (monthcheckin.equalsIgnoreCase("02")) {
                holder.txt_checkinmonth.setText("FEB");
            } else if (monthcheckin.equalsIgnoreCase("03")) {
                holder.txt_checkinmonth.setText("MAR");
            } else if (monthcheckin.equalsIgnoreCase("04")) {
                holder.txt_checkinmonth.setText("APR");
            } else if (monthcheckin.equalsIgnoreCase("05")) {
                holder.txt_checkinmonth.setText("MAY");
            } else if (monthcheckin.equalsIgnoreCase("06")) {
                holder.txt_checkinmonth.setText("JUN");
            } else if (monthcheckin.equalsIgnoreCase("07")) {
                holder.txt_checkinmonth.setText("JUL");
            } else if (monthcheckin.equalsIgnoreCase("08")) {
                holder.txt_checkinmonth.setText("AUG");
            } else if (monthcheckin.equalsIgnoreCase("09")) {
                holder.txt_checkinmonth.setText("SEP");
            } else if (monthcheckin.equalsIgnoreCase("10")) {
                holder.txt_checkinmonth.setText("OCT");
            } else if (monthcheckin.equalsIgnoreCase("11")) {
                holder.txt_checkinmonth.setText("NOV");
            } else if (monthcheckin.equalsIgnoreCase("12")) {
                holder.txt_checkinmonth.setText("DEC");
            }
            String daycheckout = hotelier_show_requests_bean.get(position).getCheckout().substring(8, 10);
            String monthcheckout = hotelier_show_requests_bean.get(position).getCheckout().substring(5, 7);
            String yearcheckout = hotelier_show_requests_bean.get(position).getCheckout().substring(0, 4);
            holder.chkout_hh.setText(hotelier_show_requests_bean.get(position).getCheckout().substring(11,16));
            holder.txt_checkoutyear.setText(yearcheckout);
            holder.txt_checkoutday.setText(daycheckout);
            if (monthcheckout.equalsIgnoreCase("01")) {
                holder.txt_checkoutmonth.setText("JAN");
            } else if (monthcheckout.equalsIgnoreCase("02")) {
                holder.txt_checkoutmonth.setText("FEB");
            } else if (monthcheckout.equalsIgnoreCase("03")) {
                holder.txt_checkoutmonth.setText("MAR");
            } else if (monthcheckout.equalsIgnoreCase("04")) {
                holder.txt_checkoutmonth.setText("APR");
            } else if (monthcheckout.equalsIgnoreCase("05")) {
                holder.txt_checkoutmonth.setText("MAY");
            } else if (monthcheckout.equalsIgnoreCase("06")) {
                holder.txt_checkoutmonth.setText("JUN");
            } else if (monthcheckout.equalsIgnoreCase("07")) {
                holder.txt_checkoutmonth.setText("JUL");
            } else if (monthcheckout.equalsIgnoreCase("08")) {
                holder.txt_checkoutmonth.setText("AUG");
            } else if (monthcheckout.equalsIgnoreCase("09")) {
                holder.txt_checkoutmonth.setText("SEP");
            } else if (monthcheckout.equalsIgnoreCase("10")) {
                holder.txt_checkoutmonth.setText("OCT");
            } else if (monthcheckout.equalsIgnoreCase("11")) {
                holder.txt_checkoutmonth.setText("NOV");
            } else if (monthcheckout.equalsIgnoreCase("12")) {
                holder.txt_checkoutmonth.setText("DEC");
            }
        }
        holder.txt_no_of_rooms.setText(hotelier_show_requests_bean.get(position).getno_of_rooms());
        holder.txt_adult.setText(hotelier_show_requests_bean.get(position).getadult());
        holder.txt_childrens.setText(hotelier_show_requests_bean.get(position).getCildren());
        holder.txt_item_info.setText(hotelier_show_requests_bean.get(position).getitem_info());
//        holder.txt_city.setText(hotelier_show_requests_bean.get(position).getcity());
        holder.txt_price.setText(hotelier_show_requests_bean.get(position).getCurrency()+" " + hotelier_show_requests_bean.get(position).getprice()+ " (Guest Budget)");
//        holder.txt_user_distance.setText(hotelier_show_requests_bean.get(position).getUser_distance());
//            String dist = holder.txt_user_distance.getText().toString();
//            String dist1 = dist.substring(0,1);
//            int distan = Integer.parseInt(dist1);
//            if(distan>=2)
//            {
//                holder.filter.setVisibility(View.GONE);
//            }
//
//
        return convertView;

    }
}

