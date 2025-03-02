package com.cheqin.booking.User;

import android.os.Bundle;
import android.util.Log;

import com.cheqin.booking.Adapter.ImageAdapter;
import com.cheqin.booking.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by user on 12-02-2016.
 */
public class FullImageUserActivity extends AppCompatActivity {

    ViewPager viewPager;
    private ArrayList<String> gallery = null;
    private int position;
    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenuserview);

        viewPager = (ViewPager) findViewById(R.id.user_fullpager);


        Bundle bundleObject = getIntent().getExtras();
        // Get ArrayList Bundle
        gallery = (ArrayList<String>) bundleObject.getSerializable("gallery_images");
        Log.e("gal", gallery.toString());


        position = bundleObject.getInt("position", position);


        adapter = new ImageAdapter(this, gallery);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);

    }
}
