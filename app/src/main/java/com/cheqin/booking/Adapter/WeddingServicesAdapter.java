package com.cheqin.booking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.Bean.WeddingService;
import com.cheqin.booking.R;

import java.util.ArrayList;
import java.util.List;

public class WeddingServicesAdapter extends ArrayAdapter<WeddingService> {

    private List<WeddingService> weddingServiceList;
    private ImageLoader imageLoader;
    private ArrayList<Long> clv;
    private Context context;
    private boolean[] checkBoxState;
    private WeddingServicesAdapter.WeddingServiceClickListener listener;

    public WeddingServicesAdapter(List<WeddingService> amenitiesbeans, Context context, ArrayList<Long> checkedIdList) {
        super(context, R.layout.wedding_list_item, amenitiesbeans);
        this.weddingServiceList = amenitiesbeans;
        this.context = context;
        this.clv = checkedIdList;
        listener = (WeddingServicesAdapter.WeddingServiceClickListener) context;
        this.checkBoxState = new boolean[weddingServiceList.size()];
    }

    private class ViewHolder {
        TextView tvName;
        NetworkImageView networkImageView;
        CheckBox chkSelected;
        LinearLayout llmain;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        WeddingServicesAdapter.ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.wedding_list_item, null);
            holder = new WeddingServicesAdapter.ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkSelected);
            holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.image1);
            holder.llmain = (LinearLayout) convertView.findViewById(R.id.llmain);

            convertView.setTag(holder);
        } else {
            holder = (WeddingServicesAdapter.ViewHolder) convertView.getTag();
        }


        WeddingService weddingVenue = weddingServiceList.get(position);
        holder.tvName.setText(weddingVenue.getName());

        if (clv.contains(weddingVenue.getId())) {
            checkBoxState[position] = true;
        }


        holder.chkSelected.setChecked(checkBoxState[position]);

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    checkBoxState[position] = true;
                } else {
                    checkBoxState[position] = false;
                }
                listener.onWeddingServiceClicked(position, ((CheckBox) v).isChecked());
            }
        });


        holder.llmain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckBox cb_item = (CheckBox) v.findViewById(R.id.chkSelected);

                cb_item.setChecked(!cb_item.isChecked());

                if (cb_item.isChecked()) {
                    checkBoxState[position] = true;
                } else {
                    checkBoxState[position] = false;
                }
                listener.onWeddingServiceClicked(position, cb_item.isChecked());
            }

        });
        return convertView;
    }

    public interface WeddingServiceClickListener {
        void onWeddingServiceClicked(int position, boolean checked);
    }
}

