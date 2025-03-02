package com.cheqin.booking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.R;
import com.cheqin.booking.User.FullImageUserActivity;
import com.cheqin.booking.utils.VolleySingleton;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by user on 12-02-2016.
 */
public class FullImageAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> img;
    LayoutInflater layoutInflater;

    public FullImageAdapter(Context context, ArrayList<String> img) {
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
        imageView.setOnClickListener(new OnImageClickListener(position));
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    class OnImageClickListener implements View.OnClickListener {

        int postion;

        // constructor
        public OnImageClickListener(int position) {
            this.postion = position;
        }

        @Override
        public void onClick(View v) {
            ArrayList<String> arr = new ArrayList<>();
            for (int i = 0; i < img.size(); i++) {
                arr.add(img.get(i));
            }


            // on selecting grid view image
            // launch full screen activity
            Intent i = new Intent(context, FullImageUserActivity.class);

            Bundle bundleObject = new Bundle();
            bundleObject.putSerializable("gallery_images", arr);
            bundleObject.putInt("position", postion);
            i.putExtras(bundleObject);
            context.startActivity(i);
        }

    }
}
