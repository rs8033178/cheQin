package com.cheqin.booking.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.cheqin.booking.Adapter.FullImageAdapter;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.DirectionsJSONParser;
import com.cheqin.booking.utils.WorkAroundMapFragment;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserShowMatchDetails extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    private TextView hotel_details_name = null;
    private TextView hotel_details_address = null;
    private TextView hotel_details_price = null;
//    private TextView hotel_details_city = null;
    private TextView hotel_details_expiry_date = null;
    private SharedPreferences image_url = null;
    private Button button_confirm = null;
    private TextView hotel_distance = null;

    private AsyncTaskListener asyncTaskListener = null;
    private Context context = null;
    private String price = null;
    private String hotel_name = null;
    private String cur_lat = null;
    private String cur_lon = null;
    private String hotel_lat = null;
    private String hotel_lon = null;
    private String expire_time = null;
    private String str_date = null;
    ViewPager viewPager;
    private ArrayList<String> gallery = null;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private LatLng CURRENT;
    private LatLng HOTEL;
    private MapFragment googleMap;
    MarkerOptions options;
    private NestedScrollView scroll;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    int mutedColor;
    Date date;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_show_match_details);

        scroll = (NestedScrollView) findViewById(R.id.nested);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        options = new MarkerOptions();

        context = UserShowMatchDetails.this;

        intent = getIntent();
//        asyncTaskListener = UserShowMatchDetails.this;

        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbar.setTitle(intent.getStringExtra("hotelname"));
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        hotel_details_name = (TextView) findViewById(R.id.hotel_detail_name);
        hotel_details_address = (TextView) findViewById(R.id.hotel_detail_address);
        hotel_details_price = (TextView) findViewById(R.id.hotel_details_price);
//        hotel_details_city = (TextView) findViewById(R.id.hotel_details_city);
        hotel_details_expiry_date = (TextView) findViewById(R.id.expire_time);
        hotel_distance = (TextView)findViewById(R.id.distance);

        image_url = getSharedPreferences("image", MODE_PRIVATE);

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.view_pager);
//        Palette.from(viewPager).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
//                collapsingToolbar.setContentScrimColor(mutedColor);
//            }
//        });


        hotel_details_name.setText(intent.getStringExtra("hotelname"));
        hotel_name = intent.getStringExtra("hotelname");
        hotel_details_address.setText(intent.getStringExtra("hoteladdr"));
        hotel_details_price.setText(intent.getStringExtra("currency")+" " + intent.getStringExtra("totalprice"));
        price = intent.getStringExtra("totalprice");
//        hotel_details_city.setText(intent.getStringExtra("hotelcity"));
        hotel_details_expiry_date.setText(image_url.getString("offer_expired", null));
        cur_lat = intent.getStringExtra("latitude");
        cur_lon = intent.getStringExtra("longitude");
        hotel_distance.setText(intent.getStringExtra("distance"));
//        Log.e("lat", cur_lat);
        hotel_lat = intent.getStringExtra("hotel_latitude");
        hotel_lon = intent.getStringExtra("hotel_longitude");
        expire_time = intent.getStringExtra("expire_time");
        str_date = intent.getStringExtra("str_date");
//        Log.e("expiry time", expire_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            date = format.parse(str_date);
            Log.e("date", date.toString());
            timerPart();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        Log.e("lat", hotel_lat);


        try {
            CURRENT = new LatLng(Double.valueOf(cur_lat),Double.valueOf(cur_lon));
            HOTEL = new LatLng(Double.valueOf(hotel_lat),Double.valueOf(hotel_lon));
        }catch (Exception e){
            CURRENT = new LatLng(12.9716,77.5946);
            HOTEL = new LatLng(12.9716,77.5946);
        }


        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawShortestPath();


        gallery = (ArrayList<String>) intent.getSerializableExtra("gallery");

//        mpref = getSharedPreferences("user_type", MODE_PRIVATE);
//        if (mpref != null) {
//            auth_token = mpref.getString("auth", null);
//        }

        if(gallery!=null && gallery.size()>0) {

            NUM_PAGES = gallery.size();
        }else
        {
            NUM_PAGES = 1;
        }

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);



        if(gallery!=null && gallery.size()>0) {
            FullImageAdapter adapter = new FullImageAdapter(this, gallery);
            viewPager.setAdapter(adapter);


           /* CirclePageIndicator indicator = (CirclePageIndicator)
                    findViewById(R.id.indicator);
            final float density = getResources().getDisplayMetrics().density;
            indicator.setFillColor(0xFFFFFFFF);
            indicator.setStrokeColor(0xFFFFFFFF);
            indicator.setStrokeWidth(1);
            indicator.setRadius(6 * density);
            indicator.setViewPager(viewPager);

            //Set circle indicator radius
            indicator.setRadius(5 * density);

            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });*/
        }


        button_confirm = (Button) findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PreparePayOnline.class);
                i.putExtra("currency",intent.getStringExtra("currency"));
                startActivity(i);
            }
        });
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

    private void timerPart() {

        Log.e("date", date.toString());

        long currentDate = Calendar.getInstance().getTime().getTime();
        long limitDate = date.getTime();
        long difference = limitDate - currentDate;
        CountDownTimer cdt = new CountDownTimer(difference, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                int days = 0;
                int hours = 0;
                int minutes = 0;
                int seconds = 0;
                String sDate = "";

                if(millisUntilFinished > DateUtils.DAY_IN_MILLIS)
                {
                    days = (int) (millisUntilFinished / DateUtils.DAY_IN_MILLIS);
                    sDate += days+"D";
                }

                millisUntilFinished -= (days*DateUtils.DAY_IN_MILLIS);

                if(millisUntilFinished > DateUtils.HOUR_IN_MILLIS)
                {
                    hours = (int) (millisUntilFinished / DateUtils.HOUR_IN_MILLIS);
                }

                millisUntilFinished -= (hours*DateUtils.HOUR_IN_MILLIS);

                if(millisUntilFinished > DateUtils.MINUTE_IN_MILLIS)
                {
                    minutes = (int) (millisUntilFinished / DateUtils.MINUTE_IN_MILLIS);
                }

                millisUntilFinished -= (minutes*DateUtils.MINUTE_IN_MILLIS);

                if(millisUntilFinished > DateUtils.SECOND_IN_MILLIS)
                {
                    seconds = (int) (millisUntilFinished / DateUtils.SECOND_IN_MILLIS);
                }

                sDate += " "+String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
//                Log.e("time", sDate);
                hotel_details_expiry_date.setText(sDate.trim());
            }

            @Override
            public void onFinish() {
                hotel_details_expiry_date.setText("Finished");
            }

        };
        cdt.start();

    }

    public void cancelAllTimers()
    {
//        Set<Map.Entry<TextView, CountDownTimer>> s = counters.entrySet();
//        Iterator it = s.iterator();
//        while(it.hasNext())
//        {
//            try
//            {
//                Map.Entry pairs = (Map.Entry)it.next();
//                CountDownTimer cdt = (CountDownTimer)pairs.getValue();
//
//                cdt.cancel();
//                cdt = null;
//            }
//            catch(Exception e){}
//        }
//        it=null;
//        s=null;
//        counters.clear();
    }

    private void drawShortestPath() {

        if (googleMap != null) {

          //  CameraPosition cameraPosition = new CameraPosition.Builder().target(CURRENT).zoom(11).build();
            //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //googleMap.setOnMarkerDragListener(UserShowMatchDetails.this);
            // Enable MyLocation Button in the Map
//            googleMap.setMyLocationEnabled(true);
            // Add new marker to the Google Map Android API V2
            route(CURRENT,HOTEL);
        }


    }

    private void route(LatLng current, LatLng hotel) {
       // googleMap.clear();
        if(hotel!=null && current!=null) {
            googleMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    googleMap.addMarker(new MarkerOptions()
                            .position(hotel).draggable(false).
                                    title(hotel_name).icon((BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon)))).showInfoWindow();

                    googleMap.addMarker(new MarkerOptions()
                            .position(current).draggable(false).
                                    title("Your Location").icon((BitmapDescriptorFactory.fromResource(R.drawable.person_icon)))).showInfoWindow();
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(CURRENT).zoom(11).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    googleMap.setOnMarkerDragListener(UserShowMatchDetails.this);

                }
            });
        }

      //  googleMap.addMarker(options.position(hotel).draggable(false).title(hotel_name).icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_icon))).showInfoWindow();
       // googleMap.addMarker(options.position(current).draggable(false).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.person_icon)));
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(current, hotel);
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.e("DragStart","DragStart");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.e("MarkerDrag","MarkerDrag");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        route(marker.getPosition(),HOTEL);
        Log.e("DragEnd","DragEnd");
    }




    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map_hotel_details);
            googleMap.getMapAsync(this);

            ((WorkAroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.polyline_map)).setListener(new WorkAroundMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    scroll.requestDisallowInterceptTouchEvent(true);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Ex downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
                Common.logException(e);
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions!=null)
            {
                //TODO
              //  googleMap.addPolyline(lineOptions);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Route not available",Toast.LENGTH_LONG).show();
            }

        }

    }



}
