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
import com.cheqin.booking.Bean.WeddingVenue;
import com.cheqin.booking.R;

import java.util.ArrayList;
import java.util.List;

public class WeddingVenueAdapter extends ArrayAdapter<WeddingVenue> {

    private List<WeddingVenue> weddingVenueList;
    private ImageLoader imageLoader;
    private ArrayList<Long> clv;
    private Context context;
    private boolean[] checkBoxState;
    private WeddingVenueAdapter.WeddingVenueClickListener listener;

    public WeddingVenueAdapter(List<WeddingVenue> amenitiesbeans, Context context, ArrayList<Long> checkedIdList) {
        super(context, R.layout.wedding_list_item, amenitiesbeans);
        this.weddingVenueList = amenitiesbeans;
        this.context = context;
        this.clv = checkedIdList;

        this.checkBoxState = new boolean[weddingVenueList.size()];
    }

    public void setListener(WeddingVenueClickListener listener) {
        this.listener = (WeddingVenueAdapter.WeddingVenueClickListener) listener;
    }

    private class ViewHolder {
        TextView tvName;
        NetworkImageView networkImageView;
        CheckBox chkSelected;
        LinearLayout llmain;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        WeddingVenueAdapter.ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.wedding_list_item, null);
            holder = new WeddingVenueAdapter.ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkSelected);
            holder.chkSelected.setVisibility(View.GONE);
            holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.image1);
            holder.llmain = (LinearLayout) convertView.findViewById(R.id.llmain);

            convertView.setTag(holder);
        } else {
            holder = (WeddingVenueAdapter.ViewHolder) convertView.getTag();
        }


        WeddingVenue weddingVenue = weddingVenueList.get(position);
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
                listener.onWeddingVenueClicked(position, ((CheckBox) v).isChecked());
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
                listener.onWeddingVenueClicked(position, cb_item.isChecked());
            }

        });
        return convertView;
    }

    public interface WeddingVenueClickListener {
        void onWeddingVenueClicked(int position, boolean checked);
    }
}
