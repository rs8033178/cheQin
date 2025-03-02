package com.cheqin.booking.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.cheqin.booking.Adapter.UserMatchesListAdapter;
import com.cheqin.booking.Bean.UserMatchesbean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserShowMatches extends AppCompatActivity implements AsyncTaskListener, LocationListener, OnMapReadyCallback {

    private static final String TAG = UserShowMatches.class.getSimpleName();
    private String URL = null;
    private HashMap<String, String> para1 = null;
    private ArrayList<UserMatchesbean> rowItems1;
    private AsyncTaskListener listener1 = null;
    private UserMatchesListAdapter userMatchesListAdapter = null;
    private Context context1 = null;
    private Progressloadingdialog pd1 = null;
    LinearLayout mapfragment = null;
    //private ListView detailsList = null;
    private String expire_time = null;

    private SharedPreferences offerid = null;
    private SharedPreferences image_url = null;
    private SharedPreferences locationPref = null;
    private SharedPreferences bookdetails = null;
    private String auth_token = null;
    MapFragment googleMap;
    String lat = null;
    String lon = null;
    LatLng latLng;
    Double latitude = Double.valueOf("20.59202");
    Double longitude = Double.valueOf("78.9623");
    private Date date;
    private String img_url = null;
    private ArrayList<String> gallery_img = null;

    private TextView txt_marker = null;

    SettingSharedPrefs ssp;
    View marker;
    TextView hotel_name;
    TextView address;
    public String hotelname;
    public String hotel_addr;
    String marker_id = null;
    int marker_position;
    //CollapsingToolbarLayout collapsingToolbar;
    String price = null;
    String expiry_time = null;
    DecimalFormat commaformatter;
    String currency;

    LinearLayout bottomSheet;
    BottomSheetBehavior mBottomSheetBehaviour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_show_matches);

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
//
//        locationPref = getSharedPreferences("latlong", MODE_PRIVATE);
//        latitude = Double.valueOf(locationPref.getString("lat", null));
//        longitude = Double.valueOf(locationPref.getString("long", null));

        commaformatter = new DecimalFormat("#,###,###");

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Button btnSeelist = findViewById(R.id.see_list_offers);
        btnSeelist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                }else{
                    mBottomSheetBehaviour.setState(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Top 3 Offers");
        //detailsList = (ListView) findViewById(R.id.listView3);
        offerid = getSharedPreferences("offer_id", MODE_PRIVATE);
        String id = offerid.getString("id", null);
//        currency=offerid.getString("currency",null);
        Log.e("id", id);
        URL = " http://api.mypillows.company/deals/request_details_json/" + id + "/get.json?";
        context1 = getApplicationContext();
//        list = (ListView) findViewById(R.id.listView3);
        listener1 = UserShowMatches.this;
        rowItems1 = new ArrayList<UserMatchesbean>();


        bottomSheet = findViewById(R.id.bottom_sheet);

        auth_token = ssp.getPRE_auth_token();

        para1 = new HashMap<>();
        para1.put("auth_token", auth_token);
        Log.e("parameters", para1.toString());

        pd1 = new Progressloadingdialog(UserShowMatches.this, "Loading...");
        pd1.setCancelable(false);
        pd1.show();

        HttpAsync httpAsync = new HttpAsync(context1, listener1, URL, para1, 1, "hoteldetails");
        httpAsync.execute();



     /* detailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(UserShowMatches.this, UserShowMatchDetails.class);
                intent.putExtra("hoteladdr", rowItems1.get(position).getHotelAddress());
                Log.e("pos",String.valueOf(position));
                intent.putExtra("hotelcity", rowItems1.get(position).getHotelCity());
                intent.putExtra("hotelname", rowItems1.get(position).getHotelName());
                SharedPreferences.Editor edt = bookdetails.edit();
                edt.putString("price", rowItems1.get(position).getTotalPrice());
                edt.commit();
                intent.putExtra("totalprice", commaformatter.format(Integer.valueOf(rowItems1.get(position).getTotalPrice())));
                intent.putExtra("gallery", rowItems1.get(position).getImg());
                intent.putExtra("id", rowItems1.get(position).getId());
                intent.putExtra("latitude", latitude.toString());
                Log.e("lat", latitude.toString());
                intent.putExtra("longitude", longitude.toString());
                intent.putExtra("hotel_latitude", rowItems1.get(position).getLatitude());
                intent.putExtra("hotel_longitude", rowItems1.get(position).getLongitude());
                intent.putExtra("distance", rowItems1.get(position).getDistance());
                expiry_time = String.valueOf(rowItems1.get(position).getDate());
                intent.putExtra("expire_time", expiry_time);
                intent.putExtra("str_date", rowItems1.get(position).getStr_date());
                intent.putExtra("currency",rowItems1.get(position).getCurrency());
//                Log.e("expire_time",rowItems1.get(position).getDate().toString());
//                Log.e("lat", lat);
                startActivity(intent);
            }
        });*/


        latLng = new LatLng(latitude, longitude);
        setUpMap();
    }

    @Override
    public void onTaskComplete(String result, String tag) {

        pd1.dismiss();
        Log.e("Result is : ", result);
        if (tag.equalsIgnoreCase("hoteldetails")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    JSONObject json = jsonObject.getJSONObject("request_detail");
                    String latilng = json.getString("latilong");
                    int latilong = latilng.lastIndexOf("||");
                    latitude = Double.valueOf(latilng.substring(0, latilong));
                    longitude = Double.valueOf(latilng.substring(latilong + 2));
                    Log.e("user_latlng", latitude.toString() + "," + longitude.toString());
                    bookdetails = getSharedPreferences("deatils", MODE_PRIVATE);
                    SharedPreferences.Editor edt = bookdetails.edit();
                    edt.putString("rooms", json.getString("no_of_rooms"));
                    edt.putString("adults", json.getString("adult"));
                    edt.putString("children", json.getString("children"));
                    edt.putString("hoteltype", json.getString("item_info"));
                    edt.commit();
                    JSONArray jsonArray = jsonObject.getJSONArray("top_five_live_offers");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            UserMatchesbean b1 = new UserMatchesbean();
                            final JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //Log.d(TAG, ""+jsonObject1.getString("user_request_id"));
                            b1.setId(jsonObject1.getString("user_request_id"));
                            expire_time = jsonObject1.getString("offer_expired");
                            String exp = expire_time.substring(0, 22);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            try {
                                date = format.parse(exp);
                                System.out.println(date);
                             //   Log.e("datematch", date.toString());
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            b1.setStr_date(exp);
                            b1.setDate(date);
                            b1.setHotelName(jsonObject1.getString("onbehalf_hotel_name"));
                            b1.setHotelAddress(jsonObject1.getString("onbehalf_hotel_addr"));
                            b1.setCurrency(jsonObject1.getString("currency_code"));
                            price = jsonObject1.getString("total_price_inr");
                            int price_pos = price.lastIndexOf(".");
                            b1.setTotalPrice(price.substring(0, price_pos));
                            b1.setHotelCity(jsonObject1.getString("user_city"));
                            image_url = getSharedPreferences("image", MODE_PRIVATE);
                            SharedPreferences.Editor editor = image_url.edit();
//                                editor.putString("image_url", jsonObject1.getString("offer_img_root"));
                            editor.putString("id", jsonObject1.getString("id"));
//                                editor.putString("image_file_name", jsonObject1.getString("buysell_img_file_name"));
                            editor.commit();
//                                calculating the distance
                            hotelname = jsonObject1.getString("onbehalf_hotel_name");
                            hotel_addr = jsonObject1.getString("onbehalf_hotel_addr");
                            lat = jsonObject1.getString("hotel_latitude");
                            b1.setLatitude(lat);
                            lon = jsonObject1.getString("hotel_longitude");
                            b1.setLongitude(lon);
                            Location selected_location = new Location("locationA");
                            selected_location.setLatitude(latitude);
                            selected_location.setLongitude(longitude);
                            Location near_locations = new Location("locationB");

                            try {
                                near_locations.setLatitude(Double.valueOf(lat));
                                near_locations.setLongitude(Double.valueOf(lon));
                            } catch (Exception e) {
                                near_locations.setLatitude(12.9716);
                                near_locations.setLongitude(77.5946);
                            }


                            double distance = selected_location.distanceTo(near_locations) / 1000;
                            Log.e("dist", String.valueOf(distance));
                            String dist = String.valueOf(distance);
                            dist = dist.substring(0, 4);
                            b1.setDistance(dist + " Kms");
                            rowItems1.add(b1);
//ToDO
                            /*googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {

                                    return null;
                                }

                                @Override
                                public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
                                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
//                                 LatLng latLng = arg0.getPosition();
                                    hotel_name = (TextView) v.findViewById(R.id.name);
//                                        address = (TextView) v.findViewById(R.id.address);
//                                        Log.e("marker.getPosition()", marker.getPosition() + "id" + marker.getId());
                                    hotel_name.setText(marker.getTitle());
                                    marker_id = marker.getId().substring(1);
                                    marker_position = Integer.valueOf(marker_id) - 1;

//                                        address.setText(rowItems1.get(Integer.parseInt(marker_id.substring(1))).getHotelAddress());
                                    return v;
                                }
                            });*/

                            txt_marker.setText(jsonObject1.getString("currency_code") + " " + price.substring(0, price_pos));

                            LatLng latlong;
                            try {
                                latlong = new LatLng(Double.valueOf(lat), Double.valueOf(lon));

                            } catch (Exception e) {
                                latlong = new LatLng(12.9716, 77.5946);
                            }

                          /*  googleMap.addMarker(new MarkerOptions().position(latlong).title(hotelname).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))).anchor(0.5f, 1)).showInfoWindow();
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latlong)      // Sets the center of the map to Mountain View
                                    .zoom(12)                   // Sets the zoom
                                    .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                            if(latlong!=null) {
                                googleMap.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(latLng).
                                                        title(hotelname).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getApplicationContext(), marker))).anchor(0.5f, 1)).showInfoWindow();
                                        final CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(latLng)      // Sets the center of the map to Mountain View
                                                .zoom(15)                   // Sets the zoom
                                                .bearing(90)                // Sets the orientation of the camera to east
                                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                                .build();
                                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                            @Override
                                            public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {

                                                return null;
                                            }

                                            @Override
                                            public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
                                                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
//                                 LatLng latLng = arg0.getPosition();
                                                hotel_name = (TextView) v.findViewById(R.id.name);
//                                        address = (TextView) v.findViewById(R.id.address);
//                                        Log.e("marker.getPosition()", marker.getPosition() + "id" + marker.getId());
                                                hotel_name.setText(marker.getTitle());
                                                marker_id = marker.getId().substring(1);
                                                marker_position = Integer.valueOf(marker_id) - 1;

//                                        address.setText(rowItems1.get(Integer.parseInt(marker_id.substring(1))).getHotelAddress());
                                                return v;
                                            }
                                        });
                                    }
                                });
                            }
//                                if (jsonArray.length() > 0) {
//                                    Log.e("jsarray", jsonArray.length() + "");
//                                    for (int k = 0; k < jsonArray.length(); k++) {
//                                        JSONObject json2 = jsonArray.getJSONObject(k);
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("image_gallery");
                            Log.e("imgarray", jsonArray1.length() + "");
                            gallery_img = new ArrayList<>();
                            if (jsonArray1.length() > 0) {
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject json1 = jsonArray1.getJSONObject(j);
                                    img_url = json1.getString("img_url");
                                    b1.setImage(img_url);
                                    gallery_img.add(json1.getString("img_url"));
                                    b1.setImg(gallery_img);
                                    Log.e("image", json1.getString("img_url"));
                                    Log.e("dhfgh", gallery_img.toString());
//                                        rowItems1.add(b1);
                                }
//                                        }
//                                    }
                            }

//                            SharedPreferences.Editor editor = sharedhoteldetails.edit();
//                            editor.putString("hotelname", jsonObject1.getString("onbehalf_hotel_name"));
//                            editor.putString("hoteladdr", jsonObject1.getString("onbehalf_hotel_addr"));
//                            editor.putString("hotelprice", jsonObject1.getString("total_price_inr"));
//                            editor.putString("hotelcity", jsonObject1.getString("user_city"));
//                            editor.putString("hotellat", jsonObject1.getString("hotel_latitude"));
//                            editor.putString("hotellong", jsonObject1.getString("hotel_longitude"));
//                            editor.commit();

                        }
//                        JSONObject joj = jsonObject.getJSONObject("my_offer");
//                        if (joj != null) {
//                            image_url = getSharedPreferences("image", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = image_url.edit();
//                            editor.putString("image_url", joj.getString("offer_img_root"));
//                            editor.putString("id", joj.getString("id"));
//                            editor.putString("image_file_name", joj.getString("buysell_img_file_name"));
//                            editor.putString("mid",joj.getString("merchant_id"));
//                            editor.commit();
//
//                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            userMatchesListAdapter = new UserMatchesListAdapter(context1, rowItems1, commaformatter);

           /* detailsList.setAdapter(userMatchesListAdapter);
            if (detailsList.getCount() == 0) {
                mapfragment.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Not Offered yet", Toast.LENGTH_LONG).show();
            }*/

        }
    }/*  detailsList.setAdapter(userMatchesListAdapter);
            if (detailsList.getCount() == 0) {
                mapfragment.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Not Offered yet", Toast.LENGTH_LONG).show();
            }*/

    private void showinfo(String hotelname, String hotel_addr) {

        final String hotelname1 = hotelname;
        final String hotel_addr1 = hotel_addr;

    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void setUpMap() {

        marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        txt_marker = (TextView) marker.findViewById(R.id.txt_marker);
        mapfragment = (LinearLayout) findViewById(R.id.mapfrag);
        googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.hotelsmap);
        googleMap.getMapAsync(this);

//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//
//            @Override
//            public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
//                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
////                LatLng latLng = arg0.getPosition();
//                hotel_name = (TextView) findViewById(R.id.name);
//                address = (TextView) findViewById(R.id.address);
//                return v;
//            }
//        });



        if(latLng!=null) {
            googleMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng).draggable(false).
                                    title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.person_icon)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 3);

                    googleMap.animateCamera(cameraUpdate);
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {

                            try {
                                Intent intent = new Intent(UserShowMatches.this, UserShowMatchDetails.class);
                                Log.e("marker", marker_id);
                                Log.e("marker_position", String.valueOf(marker_position));
                                intent.putExtra("hoteladdr", rowItems1.get(marker_position).getHotelAddress());
                                intent.putExtra("hotelcity", rowItems1.get(marker_position).getHotelCity());
                                intent.putExtra("hotelname", rowItems1.get(marker_position).getHotelName());
                                SharedPreferences.Editor edt = bookdetails.edit();
                                edt.putString("price", rowItems1.get(marker_position).getTotalPrice());
                                edt.commit();
                                intent.putExtra("totalprice", rowItems1.get(marker_position).getTotalPrice());
                                intent.putExtra("gallery", rowItems1.get(marker_position).getImg());
                                intent.putExtra("id", rowItems1.get(marker_position).getId());
                                intent.putExtra("latitude", latitude.toString());
                                Log.e("lat", latitude.toString());
                                intent.putExtra("longitude", longitude.toString());
                                intent.putExtra("hotel_latitude", rowItems1.get(marker_position).getLatitude());
                                intent.putExtra("hotel_longitude", rowItems1.get(marker_position).getLongitude());
                                intent.putExtra("distance", rowItems1.get(marker_position).getDistance());
                                intent.putExtra("str_date", rowItems1.get(marker_position).getStr_date());
                                expiry_time = String.valueOf(rowItems1.get(marker_position).getDate());
                                intent.putExtra("expire_time", expiry_time);
                                intent.putExtra("currency", rowItems1.get(marker_position).getCurrency());
                                Log.e("lat", lat);
                                startActivity(intent);
                            } catch (Exception e) {

                            }


                        }
                    });
                }
            });
        }
       /* googleMap.addMarker(new MarkerOptions().position(latLng).draggable(false).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.person_icon)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 3);
        googleMap.animateCamera(cameraUpdate);
*/

    }


    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(latitude, longitude);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

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

    @Override
    public void onTaskCancelled(String data) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserShowMatches.this, UserLiveRequests.class));
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
