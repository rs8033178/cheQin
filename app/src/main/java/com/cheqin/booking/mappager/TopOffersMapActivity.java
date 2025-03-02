package com.cheqin.booking.mappager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cheqin.booking.utils.MapViewPager;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.cheqin.booking.R;
import com.cheqin.booking.databinding.ActivityTopOffersMapBinding;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.mappager.model.TopFiveLiveOffer;
import com.cheqin.booking.mappager.model.TopOffersResponse;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopOffersMapActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = TopOffersMapActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 987;
    private static final int REQUEST_CHECK_SETTINGS = 876;
    private MapViewPager mvp;
    private SupportMapFragment map;
    private List<TopFiveLiveOffer> topFiveLiveOffers = new ArrayList<>();
    private TopOffersResponse response;
    private MapTopOfferPagerAdapter adapter;
    private Progressloadingdialog progressloadingdialog;
    private ActivityTopOffersMapBinding mDataBinding;
    private Location mCurrentLocation;
    private HttpAsync httpAsync;

    protected LocationManager locationManager;
    String browserKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_top_offers_map);
        setSupportActionBar(mDataBinding.toolbar.toolbar);
        mDataBinding.toolbar.toolbar.setTitle(R.string.top_three_ofers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.top_three_ofers);
        progressloadingdialog = new Progressloadingdialog(this, "Hold on....");
        mDataBinding.toolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mDataBinding.viewPager.setOnTouchListener((v, event) -> false);
        SharedPreference sharedPreference = new SharedPreference();
        browserKey = sharedPreference.getApiKey(getApplicationContext());

        //SharedPreferences offerid = getSharedPreferences("offer_id", MODE_PRIVATE);
        String id = getIntent().getStringExtra("offer_id");
        String url = "http://api.mypillows.company/deals/request_details_json/" + id + "/get.json?";
        //Log.d(TAG, "URL " + url);

        SettingSharedPrefs ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", ssp.getPRE_auth_token());

        httpAsync = new HttpAsync(this, listener, url, params, 1, "hoteldetails");
        progressloadingdialog.show();
        httpAsync.execute();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checlLocationPermissions();
            return;
        }
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checlLocationPermissions();
            return;
        }
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        progressloadingdialog.show();
                        httpAsync.execute();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        progressloadingdialog.show();
                        httpAsync.execute();
                        break;
                }
                break;
        }
    }


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request


    private static final int ALL_PERMISSIONS_RESULT = 1011;

    private void checlLocationPermissions() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    AsyncTaskListener listener = new AsyncTaskListener() {
        @Override
        public void onTaskCancelled(String data) {
            if (progressloadingdialog != null) progressloadingdialog.dismiss();
        }

        @Override
        public void onTaskComplete(String result, String tag) {
            try {
                if (progressloadingdialog != null) progressloadingdialog.dismiss();

                //Log.d(TAG, "onTaskComplete - " + result);


                Gson gson = new Gson();
                response = gson.fromJson(result, TopOffersResponse.class);


                SharedPreferences bookdetails = getSharedPreferences("deatils", MODE_PRIVATE);
                SharedPreferences.Editor edt = bookdetails.edit();
                edt.putString("rooms", String.valueOf(response.getRequestDetail().getNoOfRooms()));
                edt.putString("adults", String.valueOf(response.getRequestDetail().getAdult()));
                edt.putString("children", String.valueOf(response.getRequestDetail().getChildren()));
                edt.putString("hoteltype", response.getRequestDetail().getItemInfo());
                edt.commit();


                topFiveLiveOffers.clear();
                topFiveLiveOffers.addAll(response.getTopFiveLiveOffers());
                mDataBinding.viewPager.setPageMargin(MapUtils.dp(TopOffersMapActivity.this, 18));
                if (response != null) {
                    //    Log.v("vinod","response"+response);
                    //Toast.makeText(getApplicationContext(),""+ topFiveLiveOffers.size(),Toast.LENGTH_SHORT).show();
                    adapter = new MapTopOfferPagerAdapter(getSupportFragmentManager(), topFiveLiveOffers, response, TopOffersMapActivity.this, mCurrentLocation);

                    MapUtils.setMargins(mDataBinding.viewPager, 0, 0, 0, MapUtils.getNavigationBarHeight(TopOffersMapActivity.this));

                    double reqLatitude = response.getRequestDetail().getLatitude() != null ? Double.parseDouble(response.getRequestDetail().getLatitude()) : 0;
                    double reqLongitude = response.getRequestDetail().getLongitude() != null ? Double.parseDouble(response.getRequestDetail().getLongitude()) : 0;
                    Location selectedLocation = new Location("locationA");
                    selectedLocation.setLongitude(reqLongitude);
                    selectedLocation.setLatitude(reqLatitude);
                    mvp = new MapViewPager.Builder(TopOffersMapActivity.this)
                            .mapFragment(map)
                            .viewPager(mDataBinding.viewPager)
                            .position(2)
                            .adapter(adapter)
                            .callback(mapsCallback)
                            .setReqLocation(selectedLocation)
                            .build();


                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };


    MapViewPager.Callback mapsCallback = new MapViewPager.Callback() {
        @Override
        public void onMapViewPagerReady() {
            mDataBinding.viewPager.setCurrentItem(1);
            mvp.getMap().setPadding(
                    0,
                    MapUtils.dp(TopOffersMapActivity.this, 40),
                    MapUtils.getNavigationBarWidth(TopOffersMapActivity.this),
                    mDataBinding.viewPager.getActualHeight() + MapUtils.getNavigationBarHeight(TopOffersMapActivity.this));
        }
    };


    @Override
    public void onLocationChanged(Location location) {
        //Log.d(TAG,"onLocationChanged "+location.getLongitude());
        mCurrentLocation = location;
        SharedPreferences sp = getSharedPreferences("currentlocation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("current_longitude", String.valueOf(mCurrentLocation.getLongitude()));
        editor.putString("current_latitude", String.valueOf(mCurrentLocation.getLatitude()));
        editor.apply();
        locationManager = null;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
