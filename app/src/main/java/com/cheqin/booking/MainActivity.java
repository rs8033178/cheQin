package com.cheqin.booking;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.utils.Common;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private int[] gallery = null;
    private static final int[] gallery = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,R.drawable.slide4,R.drawable.slide5,R.drawable.slide6};
    private static int NUM_PAGES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

//        gallery = new int[]{R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4, R.drawable.slide5, R.drawable.slide6};

//        if(gallery!=null && gallery.length()>0) {
//
//            NUM_PAGES = gallery.length();
//        }else
//        {
//            NUM_PAGES = 1;
//        }
    }
    public void user(View view)
    {
        if (Common.isNetworkAvailable(getApplicationContext())) {
            Intent user = new Intent(MainActivity.this, UserBooking.class);
            user.putExtra("type", "buyer");
            user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(user);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Check Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
    public void hotelier(View view)
    {
        Intent hotel=new Intent(MainActivity.this,LoginActivity.class);
        hotel.putExtra("type","buysell");
        hotel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(hotel);
        finish();
    }


    @Override
    public void onBackPressed() {
        Common.ExitAppDialog(MainActivity.this);
    }
}