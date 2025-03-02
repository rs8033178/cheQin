package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cheqin.booking.Bean.Hotelier_View_Other_Offer_bean;
import com.cheqin.booking.R;

import java.util.List;

public class Hotelier_View_Others_Offer_Adapter extends ArrayAdapter<Hotelier_View_Other_Offer_bean> {
    Activity context;
    private List<Hotelier_View_Other_Offer_bean> Hotelier_View_Other_Offer_bean = null;

    public Hotelier_View_Others_Offer_Adapter(Activity context, List<Hotelier_View_Other_Offer_bean> rowItems) {
        super(context, R.layout.hotelier_view_other_offer_list_items, rowItems);
        this.context = context;
        Hotelier_View_Other_Offer_bean = rowItems;
    }

    private class ViewHolder {
        TextView txt_hotelier_offer_expired;
        TextView txt_hotelier_offer_price;
        TextView txt_hotelier_total_price;
        TextView other_hotel_rank;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.hotelier_view_other_offer_list_items, null);
            holder = new ViewHolder();
            holder.txt_hotelier_offer_expired = (TextView) convertView.findViewById(R.id.hotelier_offered_till);
            holder.txt_hotelier_offer_price = (TextView) convertView.findViewById(R.id.hotelier_offered_price);
            holder.txt_hotelier_total_price = (TextView) convertView.findViewById(R.id.hotelier_total_price);
            holder.other_hotel_rank = (TextView) convertView.findViewById(R.id.other_hotel_rank);
            convertView.setTag(holder);


        } else
            holder = (ViewHolder) convertView.getTag();


        holder.txt_hotelier_offer_price.setText(Hotelier_View_Other_Offer_bean.get(position).getCurrency()+" "+Hotelier_View_Other_Offer_bean.get(position).getHotelier_offered_price());
        holder.txt_hotelier_total_price.setText(Hotelier_View_Other_Offer_bean.get(position).getCurrency()+" "+Hotelier_View_Other_Offer_bean.get(position).getHotelier_total_price());
        holder.txt_hotelier_offer_expired.setText(Hotelier_View_Other_Offer_bean.get(position).getHotelier_offered_till().substring(0, 10));

       // for (int rank = 0; rank <= Hotelier_View_Other_Offer_bean.size(); rank++){
            holder.other_hotel_rank.setText((position+1)+"");
       // }
        return convertView;
    }

}
