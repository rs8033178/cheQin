package com.cheqin.booking.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.Bean.NotificationBean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.VolleySingleton;

import java.util.ArrayList;


/**
 */
public class NotificationListAdapter extends BaseAdapter{
	private ArrayList<NotificationBean> beans = null;
	private Context context = null;
	private ImageLoader imageLoader = null;
	private static LayoutInflater inflater = null;
	TextView tv_name;
	NetworkImageView itemImg;


	public NotificationListAdapter(Context context2, ArrayList<NotificationBean> beans) {
		// TODO Auto-generated constructor stub
		this.context = context2;
		this.beans = beans;
		imageLoader = VolleySingleton.getInstance(context).getImageLoader();
	}

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}



	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_notification_list, null);
		}
		NotificationBean item = beans.get(position);

		tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		itemImg = (NetworkImageView) convertView.findViewById(R.id.itemImage);

		tv_name.setText(item.getTitle().toString());

		// DisplayImage function from ImageLoader Class
		if(item.getImagepath().equalsIgnoreCase("null") || item.getImagepath().equalsIgnoreCase("")){
			itemImg.setImageUrl(Constants.NO_IMAGE, imageLoader);
		}else{
			itemImg.setImageUrl(item.getImagepath(), imageLoader);
		}
		return convertView;
	}

}
