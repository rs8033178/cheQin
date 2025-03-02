package com.cheqin.booking.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cheqin.booking.R;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.HttpAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 11-12-2015.
 */
public class Oyorooms extends AppCompatActivity implements AsyncTaskListener, OnMapReadyCallback {
    MapFragment googleMap;
    AsyncTaskListener listener = null;
    HashMap<String, String> param = null;
    private String browserKey;
    String Url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private LatLng latLng = null;
    private ProgressDialog pd1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oyorooms);
        listener = Oyorooms.this;
        browserKey = new SharedPreference().getApiKey(getApplicationContext());
        googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map_hotel_details);
        googleMap.getMapAsync(this);

        // googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentmap)).getMap();

        param = new HashMap<>();
        param.put("location", "12.9887093,77.5937212");
        param.put("radius", "15000");
        param.put("types", "lodging");
        param.put("name", "oyo");
        param.put("key", browserKey);
        pd1 = new ProgressDialog(Oyorooms.this);
        pd1.setMessage("Loading");
        pd1.setCancelable(true);
        pd1.setIndeterminate(true);
        pd1.show();
        new HttpAsync(Oyorooms.this, listener, Url, param, 1, null).execute();

    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
        pd1.dismiss();
        Log.e("result", result);

        try {
            JSONObject job = new JSONObject(result);
            JSONArray jarray = job.getJSONArray("results");
            Log.e("res", jarray.toString());
            if (jarray.length() > 0) {
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject job1 = jarray.getJSONObject(i);
                    Log.e("geo", job1.toString());
                    JSONObject job2 = job1.getJSONObject("geometry");
                    JSONObject job3 = job2.getJSONObject("location");
                    String lat = job3.getString("lat");
                    Log.e("lat", lat);
                    String lng = job3.getString("lng");
                    final String name = job1.getString("name");
                    Log.e("name", name);
                    final String hoteladdr = job1.getString("vicinity");
                    String icon = job1.getString("icon");
                   // Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(lat), Double.valueOf(lng))).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                   if(lat!=null && lng!=null) {

                       googleMap.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lat));
                                googleMap.addMarker(new MarkerOptions()
                                        .position(latLng).
                                                title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
                                final CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(latLng)      // Sets the center of the map to Mountain View
                                        .zoom(15)                   // Sets the zoom
                                        .bearing(90)                // Sets the orientation of the camera to east
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                ;
                            }
                        });
                    }
                   // marker.showInfoWindow();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Common.logException(e);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        latLng = new LatLng(12.9887093, 77.5937212);
        // googleMap.setMyLocationEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        googleMap.animateCamera(cameraUpdate);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }
}
