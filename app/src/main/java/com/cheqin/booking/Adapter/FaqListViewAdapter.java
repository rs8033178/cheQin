package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cheqin.booking.Bean.bean_faq;
import com.cheqin.booking.R;

import java.util.List;

public class FaqListViewAdapter extends ArrayAdapter<com.cheqin.booking.Bean.bean_faq> {
    Activity context;
    private List<bean_faq> bean_faq = null;

    public FaqListViewAdapter(Activity context, List<bean_faq> rowItems) {
        super(context, R.layout.faq_item, rowItems);
        this.context = context;
        bean_faq = rowItems;
    }
    private class ViewHolder {
        TextView txt_que;
        TextView txt_ans;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView=mInflater.inflate(R.layout.faq_item, null);
            holder.txt_que = (TextView) convertView.findViewById(R.id.question);
            holder.txt_ans = (TextView) convertView.findViewById(R.id.answers);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_que.setText(bean_faq.get(position).getQue());
        holder.txt_ans.setText(bean_faq.get(position).getAns());
        return convertView;
    }

}
