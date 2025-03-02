package com.cheqin.booking.mappager;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cheqin.booking.R;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.modelplaces.Photo;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by satyanarayana.avv on 12-03-2016.
 */
public class SlideShowPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<Photo> photoList;
String browserKey;
    public SlideShowPagerAdapter(Context context, List<Photo> photoList) {
        this.mContext = context;
        this.photoList = photoList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        browserKey=new SharedPreference().getApiKey(context);

    }

    @Override
    public int getCount() {
        int size = photoList.size();
        return size > 5 ? 5 : size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.slideshow_pager_item, container, false);
        String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoList.get(position).getPhotoReference() + "&key="+browserKey;
        //Log.d("SlideShowPagerAdapter", imageUrl);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.slide_image);
        Glide.with(mContext)
                .load(imageUrl)
                //.placeholder(R.drawable.image_placeholder)
                //.fitCenter()
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}