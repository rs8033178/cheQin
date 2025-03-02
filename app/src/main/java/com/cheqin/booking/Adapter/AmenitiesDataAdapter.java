package com.cheqin.booking.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.R;
import com.cheqin.booking.Bean.Amenitiesbean;
import com.cheqin.booking.utils.VolleySingleton;

public class AmenitiesDataAdapter extends ArrayAdapter<Amenitiesbean> {

    private List<Amenitiesbean> amenitiesbeanList;
    private ImageLoader imageLoader;
    public static ArrayList<String> checkedIdList2;
    ArrayList<String> clv;
    Context context;
    ListView recyclerView;
    boolean[] checkBoxState;
    OnCheckBoxClicked listener;

    public AmenitiesDataAdapter(List<Amenitiesbean> amenitiesbeans, Context context, ArrayList<String> checkedIdList) {
        super(context, R.layout.cardview_row, amenitiesbeans);
        this.amenitiesbeanList = amenitiesbeans;
        this.context = context;
//        checkedIdList2= null;
//        checkedIdList2 = checkedIdList;
        this.clv = checkedIdList;
        listener = (OnCheckBoxClicked) context;
        this.checkBoxState =new boolean[amenitiesbeanList.size()];
    }

    private class ViewHolder {
        TextView tvName;
        NetworkImageView networkImageView;
        CheckBox chkSelected;
        LinearLayout llmain;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.cardview_row, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkSelected);
            holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.image1);
            holder.llmain = (LinearLayout) convertView.findViewById(R.id.llmain);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Amenitiesbean amenitiesx = amenitiesbeanList.get(position);
        holder.tvName.setText(amenitiesx.getName());

        holder.networkImageView.setImageUrl(amenitiesx.getImage(), VolleySingleton
                .getInstance(context)
                .getImageLoader());

        // Log.e("amenitiesbean",amenitiesbean.toString());
        if (clv.contains(amenitiesx.getId())) {

            checkBoxState[position] = true;
        }

        //VITAL PART!!! Set the state of the
        //CheckBox using the boolean array
        holder.chkSelected.setChecked(checkBoxState[position]);
        //for managing the state of the boolean
        //array according to the state of the
        //CheckBox

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (((CheckBox) v).isChecked()){
                    checkBoxState[position] = true;
                //checkedIdList2.add(amenitiesbeanList.get(position).getId());
                }else{
                    checkBoxState[position] = false;
               // checkedIdList2.remove(amenitiesbeanList.get(position).getId());
                }
                  listener.oncbClicked(position, ((CheckBox) v).isChecked());
            }
        });


        holder.llmain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckBox cb_item = (CheckBox) v.findViewById(R.id.chkSelected);

                cb_item.setChecked(!cb_item.isChecked());

                if (cb_item.isChecked()){
                    checkBoxState[position] = true;
                  // checkedIdList2.add(amenitiesbeanList.get(position).getId());
                } else{
                    checkBoxState[position] = false;
                  //  checkedIdList2.remove(amenitiesbeanList.get(position).getId());
                }
                listener.oncbClicked(position, cb_item.isChecked());
            }

        });




//        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    CheckBox cb = (CheckBox) v;
////                    Amenitiesbean amenitiesbean = (Amenitiesbean) cb.getTag();
////                    amenitiesbean.setSelected(cb.isChecked());
//                if (cb.isChecked()) {
//                    checkedIdList.add(amenitiesbeanList.get(position).getId());
//                    Log.e("checked", amenitiesbeanList.get(position).getId().toString());
//                } else {
//                    checkedIdList.remove(amenitiesbeanList.get(position).getId());
//                }
//            }
//        });
//
//        holder.llmain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v.findViewById(R.id.chkSelected);
//                cb.setChecked(!cb.isChecked());
//                Amenitiesbean amenitiesbean = (Amenitiesbean) cb.getTag();
//                amenitiesbean.setSelected(cb.isChecked());
//                if (cb.isChecked()) {
//                    checkedIdList.add(amenitiesbeanList.get(position).getId());
//                    Log.e("checked", amenitiesbeanList.get(position).getId().toString());
//                } else {
//                    checkedIdList.remove(amenitiesbeanList.get(position).getId());
//                }
//            }
//        });
//
//
//
//
//        holder.chkSelected.setChecked(amenitiesbean.isSelected());
//        holder.chkSelected.setTag(amenitiesbean); // This line is important.

        return convertView;
    }

    public interface OnCheckBoxClicked {
        void oncbClicked(int position, boolean checked);
    }
}