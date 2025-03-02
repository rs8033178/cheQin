package com.cheqin.booking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.VolleySingleton;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;


public class ImageAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> img;
    LayoutInflater layoutInflater;

    public ImageAdapter(Context context, ArrayList<String> img) {
        this.context = context;
        this.img = img;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.galleryslide_images, container, false);
        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.networkimage);
//        LinearLayout.LayoutParams vp =
//                new LinearLayouct.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(vp);
//        imageView.getLayoutParams().height=300;
//        imageView.setScaleType(NetworkImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageUrl(img.get(position), VolleySingleton.getInstance(context).getImageLoader());
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}