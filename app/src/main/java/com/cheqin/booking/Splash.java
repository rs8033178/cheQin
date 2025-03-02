
package com.cheqin.booking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Bean.Amenitiesbean;
import com.cheqin.booking.Bean.Hoteltypebean;
import com.cheqin.booking.Bean.RoomTypeBean;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.gcm.SharedPreference1;
import com.cheqin.booking.gcm.SharedPreferenceRoom;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.GooglePlayVersionCheck;
import com.cheqin.booking.utils.GoolgeplayVersionListener;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.InternetUtils;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/* This class is intended  to load the splash activity image, in the background we check the user location is switched on or not
 if not switched on we show a pop up to user to switch on automatically.
 We also capture device parameter like IMEI to restrict the user accounts. We capture the latitude and longitude and convert to
 locality, city, state,country  to load the ads for Users current location
 */

public class Splash extends AppCompatActivity implements AsyncTaskListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoolgeplayVersionListener {

    boolean isForceUpdate = true;
    private static final String TAG = Splash.class.getSimpleName();
    private TelephonyManager tel;
    private static String imei = null;

    private SharedPreferences mpref = null;

    private int version_mobile;
    private Handler handler = null;
    private int version_play;

    private static int SPLASH_TIME_OUT = 1000;

    private Progressloadingdialog progressDialog1 = null;

    private HashMap<String, String> para = null;
    private HashMap<String, String> para2 = null;
    private AsyncTaskListener asyncTaskListener = null;
    private List<Amenitiesbean> amenitiesbeanList;


    private List<Hoteltypebean> hotelcategories;
    private List<RoomTypeBean> roomTypeBeanList;
    //    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    private static final int REQ_GPS =100 ;

    SettingSharedPrefs ssp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashsc);

        mpref = getSharedPreferences("user_type", MODE_PRIVATE);
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
//        locationPref = getSharedPreferences("latlong", MODE_PRIVATE);
        asyncTaskListener = Splash.this;

        handler = new Handler();
        checkConnection();
        //checkInternetAndLaunch();
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.anim_splash).into(imageView);
    }

    public void checkInternetAndLaunch() {
        if (InternetUtils.isInternetAvailable() && InternetUtils.isNetworkAvailable(getApplicationContext())) {
            new GooglePlayVersionCheck(getApplicationContext(), Splash.this).execute();
        } else {
            ShowSnackBar();
        }
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        } else {
            checkConnection();
        }
    }

    /**
     * Method to show update dialog
     */
    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Splash.this);
        // String title = " <font color='#FF5A5F'><b>Utel</b></font>";
        //   alertDialogBuilder.setTitle(Html.fromHtml(title));
        alertDialogBuilder.setMessage("New Update Available!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Splash.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));

                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse("https://play.google.com/store/apps/details?id=bargainforstays.utel"));
                startActivity(in);
                dialog.cancel();
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("SKIP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*if (isForceUpdate) {
                    finish();
                }*/
                checkConnection();
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }


    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {
            getamenities();
//            getLocation();
        } else {
            // Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            ShowSnackBar();
        }
    }


    private void getamenities() {

//        latitude = Double.valueOf(locationPref.getString("lat", null));
//        longitude = Double.valueOf(locationPref.getString("long", null));
//        para = new HashMap<String, String>();
//        para.put("latlng", latitude + "," + longitude);
//        para.put("sensor", String.valueOf(false));
//        Log.e("locPara", para.toString());
//        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), asyncTaskListener, URL, para, 1, "location");
//        httpAsync.execute();

        amenitiesbeanList = new ArrayList<Amenitiesbean>();
        hotelcategories = new ArrayList<>();
        roomTypeBeanList = new ArrayList<>();
        para2 = new HashMap<>();
        String url = Constants.BASE_URL + "user_requests/list_HTypes_N_amenities/get.json";
        new HttpAsync(getApplicationContext(), asyncTaskListener, url, para2, 1, "amenities").execute();


//        para2 = new HashMap<>();
//        new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + "user_requests/list_HTypes_N_amenities/get.json", para2, 2, "hotel_categories").execute();
    }

    private void ShowSnackBar() {
//        if (progressDialog1 != null) {
//            progressDialog1.dismiss();
//        } else {

        String styledText = "<font color='black'>No Internet Connection</font>.";
        // msnackBar;
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnection();
                    }
                })
                .setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();


        snackBarView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
//        }
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
        if (result.equalsIgnoreCase("fail")) {
            Toast.makeText(this, getResources().getString(R.string.error_generic) + result, Toast.LENGTH_LONG).show();
            ShowSnackBar();
        } else if (tag.equalsIgnoreCase("amenities")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("amenities");
                SharedPreference sharedPreference = new SharedPreference();
                SharedPreference apikey = new SharedPreference();

                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);

                        Amenitiesbean amenity = new Amenitiesbean();
                        amenity.setName(jo.getString("name"));
                        amenity.setId(jo.getString("id"));
                        amenity.setImage(jo.getString("img_url"));
                        amenitiesbeanList.add(amenity);
                    }
                    apikey.setApiKey(getApplicationContext(), jsonObject.getString("google_api_keycode"));
                    sharedPreference.saveFavorites(this, amenitiesbeanList);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("hotel_types");

                    //save wedding details
                    JSONArray weddingVendors = jsonObject.getJSONArray("wedding_vendors");
                    JSONArray weddingVenues = jsonObject.getJSONArray("wedding_venues");
                    SharedPreferences weddingPrefs = getSharedPreferences("wedding", MODE_PRIVATE);
                    SharedPreferences.Editor edt = weddingPrefs.edit();
                    edt.putString("wedding_vendors", weddingVendors.toString());
                    edt.putString("wedding_venues", weddingVenues.toString());
                    edt.apply();


                    SharedPreference1 sharedPreference1 = new SharedPreference1();
                    if (jsonArray1.length() > 0) {
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            Hoteltypebean hoteltypebean = new Hoteltypebean();
                            hoteltypebean.setId(jsonObject1.getString("id"));
                            hoteltypebean.setName(jsonObject1.getString("name"));
                            hotelcategories.add(hoteltypebean);
                        }
                        sharedPreference1.saveFavorites(this, hotelcategories);
                    }
                    JSONArray room_types = jsonObject.getJSONArray("room_types");
                    SharedPreferenceRoom sharedPreferenceRoom = new SharedPreferenceRoom();
                    sharedPreferenceRoom.saveFavorites(this, room_types.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }
            handler();
        }
    }

    public void handler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ssp.getPRE_user_login()) {
//                    startActivity(new Intent(getApplicationContext(), UserBooking.class));
                    Intent i = new Intent(getApplicationContext(), UserBooking.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else if (ssp.getPRE_hotelier_login()) {
                    Toast.makeText(getApplicationContext(), "Please Signup", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), HotelierShowRequests.class));
                    // Intent i = new Intent(getApplicationContext(), HotelierShowRequests.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
                    //finish();

                } else if (ssp.getFirstLaunch()) {
                    Intent i = new Intent(getApplicationContext(), UserBooking.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                ssp.setFirstLaunch(true);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
