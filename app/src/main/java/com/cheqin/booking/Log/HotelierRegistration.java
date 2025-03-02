package com.cheqin.booking.Log;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.R;
import com.cheqin.booking.Verify;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.cardview.widget.CardView;

/**
 * Created by user on 14-10-2015.
 */
public class HotelierRegistration extends Activity implements AsyncTaskListener, OnMapReadyCallback {
    private EditText first_name;
    private EditText last_name;
    private EditText emailText;
    private EditText passwordText;
    protected EditText phone;
    private AutoCompleteTextView autoselectcity = null;
    protected Button signupButton;
    protected TextView loginLink;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    private Progressloadingdialog progressDialog = null;
    private LocationManager locManager;
    //    private LocationListener locListener = new MyLocationListener();
    private boolean gps_enabled = false;
    protected Double longi, lati;
    protected TextView nonmember1;
    //    protected AutoCompleteTextView autoselecthotel = null;
    public static TextView hotelnotfound;

    private HashMap<String, String> parameter = null;
    private int citySelected = 0;
    private String text = "";
    private static final String TAG_RESULT = "predictions";
    private String browserKey ;
    private List<String> cities = null;
    private ArrayAdapter<String> adpt = null;
    private HashMap<String, String> paramap = null;
    public static MapFragment googleMap;
    CardView cardmap;
    SharedPreferences locationPref;
    String Authtoken = null;
    private String city = "";
    private String city_place_id = null;
    private HashMap<String,String> para_lat = null;

    SettingSharedPrefs ssp;


//    public class MyLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//                longi = location.getLongitude();
//                lati = location.getLatitude();
//                longitude = longi.toString();
//                latitude = lati.toString();
//                SharedPreferences shrp1 = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor1 = shrp1.edit();
//
//                editor1.putString("longitude", longitude);
//                editor1.putString("latitude", latitude);
//                editor1.commit();
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotelier_reg);
        browserKey=new SharedPreference().getApiKey(getApplicationContext());

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        first_name = (EditText) findViewById(R.id.f_name);
        cardmap = (CardView) findViewById(R.id.card_view);
        hotelnotfound = (TextView) findViewById(R.id.hotelnotfound);

        last_name = (EditText) findViewById(R.id.l_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        phone = (EditText) findViewById(R.id.input_phone);
        autoselectcity = (AutoCompleteTextView) findViewById(R.id.city);
        signupButton = (Button) findViewById(R.id.btn_signup);
        nonmember1 = (TextView) findViewById(R.id.login_link);
        locManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        String udata = "Already a member? Login";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        nonmember1.setText(content);


        googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.hotelmap);
        googleMap.getMapAsync(this);
        locationPref = getSharedPreferences("latlong", MODE_PRIVATE);

//        if (locationPref != null) {
//            lati = Double.valueOf("12.9715987");
//            longi = Double.valueOf("77.5945627");
//        }

        final CameraPosition cameragoogle = new CameraPosition.Builder()
                .target(new LatLng(12.9715987, 77.5945627))      // Sets the center of the map to Mountain View
                .zoom(8)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
       // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameragoogle));


        listener = HotelierRegistration.this;

        TextWatcher textWatcher;
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((first_name.getText().toString().equalsIgnoreCase("")) || (last_name.getText().toString().equalsIgnoreCase("")) ||
                        (emailText.getText().toString().equalsIgnoreCase("")) || (passwordText.getText().toString().equalsIgnoreCase("")) ||
                        (phone.getText().toString().equalsIgnoreCase("")) || (autoselectcity.getText().toString().equalsIgnoreCase(""))) {
                    signupButton.setBackgroundResource(R.drawable.button_unselect);
                    signupButton.setClickable(false);
                } else {
                    signupButton.setBackgroundResource(R.drawable.btn_login_bg);
                    signupButton.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        first_name.addTextChangedListener(textWatcher);
        last_name.addTextChangedListener(textWatcher);
        emailText.addTextChangedListener(textWatcher);
        passwordText.addTextChangedListener(textWatcher);
        phone.addTextChangedListener(textWatcher);
        autoselectcity.addTextChangedListener(textWatcher);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard();

                try {
                    gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }
                if (!gps_enabled) {
//                    Toast.makeText(getApplicationContext(), "Sorry, location is not determined. Please enable location providers", Toast.LENGTH_LONG).show();
                }

                if ((!first_name.getText().toString().equalsIgnoreCase("")) && (!last_name.getText().toString().equalsIgnoreCase("")) &&
                        (!emailText.getText().toString().equalsIgnoreCase("")) && (!passwordText.getText().toString().equalsIgnoreCase("")) &&
                        (!phone.getText().toString().equalsIgnoreCase("")) && (!autoselectcity.getText().toString().equalsIgnoreCase(""))) {
                    signupButton.setBackgroundResource(R.color.gray_btn_bg_color);
                    signupButton.setClickable(false);

//                if (!validatefirstname(first_name.getText().toString())) {
//                    first_name.setError("Enter at least 2 characters");
//                } else if (!validatelastname(last_name.getText().toString())) {
//                    last_name.setError("Enter at least 1 character");
                    if (!validatemail(emailText.getText().toString())) {
                        emailText.setError("enter a valid email address");
                    } else if (!validatepassword(passwordText.getText().toString())) {
                        passwordText.setError("Password should be minimum of 6 characters");
//                } else if (!validatephone(phone.getText().toString())) {
//                    phone.setError("Enter a valid mobile number");
                    } else if (autoselectcity.getText().toString().equalsIgnoreCase("")) {
                        autoselectcity.setError("Select City");
                    } else {

                        checkConnection();
                    }
                } else {

                    signupButton.setClickable(true);
                }
            }
        });


        autoselectcity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                citySelected = 0;
                text = autoselectcity.getText().toString();
                text = text.trim();
                text.split(" ");

                //Log.e("ontext change", charSequence.toString());
                address(text);
                city = "";
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            private void address(String trim) {
                // TODO Auto-generated method stub


                //				search_text[0]=search_text[0].trim();
                if (trim.length() == 0) {
//                    pb.setVisibility(View.GONE);
                }
                if (trim.contains(" ")) {
                    Log.e("entered in the space", "the space is accepted");
                } else if (trim.length() > 0) {
                    parameter = new HashMap<>();
                    parameter.put("input", trim);
                    parameter.put("radius", "50");
                    parameter.put("types", "(cities)");
                    parameter.put("sensor", "true");
                    parameter.put("key", browserKey);
                  //  Log.e("parameters", parameter.toString());

                    // add parameters here
                    //input=" + trim + "&types=(cities)&location=12.76999,77.44696&radius=50address&sensor=true&key=" + browserKey;
                    new HttpAsync(getApplicationContext(), listener, Constants.AUTOCOMPLETE, parameter, 1, "autocompletecity").execute();
                }
            }
        });

        autoselectcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = adpt.getItem(position).toString();
            }
        });

        nonmember1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent back = new Intent(HotelierRegistration.this, LoginActivity.class);
//                startActivity(back);
                finish();

            }
        });

        cardmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   startActivity(new Intent(HotelierRegistration.this, Selecthotel.class));
            }
        });

//        googleMap.addMarker(new MarkerOptions().position(new LatLng(lati, longi)).title(sharedPreferences.getString("hotel_name", null)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//        final CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(lati, longi))      // Sets the center of the map to Mountain View
//                .zoom(15)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        String lat = sharedPreferences.getString("lat",null);
//        String lon = sharedPreferences.getString("long",null);
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)))


//        autoselecthotel.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                citySelected = 0;
//                text = autoselecthotel.getText().toString();
//                text = text.trim();
//                text.split(" ");
//
//                Log.e("ontext change", charSequence.toString());
//                address(text);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//
//            private void address(String trim) {
//
//
//                //				search_text[0]=search_text[0].trim();
//                if (trim.length() == 0) {
//                }
//                if (trim.contains(" ")) {
//                    Log.e("entered in the space", "the space is accepted");
//                } else if (trim.length() > 0) {
//                    parameter = new HashMap<>();
//                    parameter.put("input", trim);
//                    parameter.put("type", "lodging");
//                    parameter.put("country", "india");
//                    parameter.put("location", "12.76999,77.44696");
//                    parameter.put("radius", "500");
//                    parameter.put("sensor", "true");
//                    parameter.put("key", browserKey);
//                    Log.e("parameters", parameter.toString());
//
//                    // add parameters here
//
//                    //input=" + trim + "&types=(cities)&location=12.76999,77.44696&radius=50address&sensor=true&key=" + browserKey;
//                    new HttpAsync(HotelierRegistration.this, listener, Constants.AUTOCOMPLETE, parameter, 1, "autocompletehotel").execute();
//                }
//            }
//        });
//
//        autoselecthotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                paramap = new HashMap<>();
//                paramap.put("key", browserKey);
//                paramap.put("address", autoselecthotel.getText().toString());
//                Log.e("param", paramap.toString());
//                new HttpAsync(HotelierRegistration.this, listener, Constants.GEOCODE, paramap, 1, "maps").execute();
//
//            }
//        });
    }

    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {
            doHotelierRegistration();
        } else {
            //Toast.makeText(getApplicationContext(),"Internet is not Avilable",Toast.LENGTH_SHORT).show();
            ShowSnackBar();
        }
    }

    private void ShowSnackBar() {
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
        snackBarView.setBackgroundColor(Color.parseColor("#ffffff")); // snackbar background color
        // snackbar.setActionTextColor(Color.parseColor("#000000")); // snackbar action text color
        snackbar.show();
    }

    private void doHotelierRegistration() {

        progressDialog = new Progressloadingdialog(HotelierRegistration.this, "Signing Up..");
        progressDialog.setCancelable(false);
        progressDialog.show();

//                    SharedPreferences lat = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                    final String longitud = lat.getString("longitude", longitude);
//                    final String latitud = lat.getString("latitude", latitude);
        String passwd = GenerateAuthtoken.encryptpassregistration(passwordText.getText().toString());
        //Log.e("encry password", passwd);
        para = new HashMap<>();
        para.put("user[fname]", first_name.getText().toString());
        para.put("user[lname]", last_name.getText().toString());
        para.put("user[email]", emailText.getText().toString());
        para.put("user[password]", passwd);
        para.put("address[mobile]", phone.getText().toString());
        para.put("user_type", "buysell");
        para.put("seller_type", "Hotelier");
        para.put("address[city]", autoselectcity.getText().toString());
        para.put("cityLatLong", 12.98789 + "||" + 77.77778);
        //Log.e("params", para.toString());

        Authtoken = GenerateAuthtoken.generate(emailText.getText().toString(), passwordText.getText().toString(), "normal");

        HttpAsync tokenAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.REGISTRATION, para, 2, "register");
        tokenAsync.execute();


    }

    private boolean validatephone(String phoneno) {
        if (phoneno != null && phoneno.length() > 7 && phoneno.length() < 11) {
            return true;
        } else {
            return false;

        }
    }


    private boolean validatepassword(String passwd) {
        if (passwd != null && passwd.length() >= 6) {
            return true;
        } else {
            return false;

        }
    }

    private boolean validatemail(String mail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    private boolean validatelastname(String last) {
        if (last != null && last.length() >= 1) {
            return true;
        } else {
            return false;

        }
    }

    private boolean validatefirstname(String first) {
        if (first != null && first.length() >= 2) {
            return true;
        } else {
            return false;

        }
    }


    @Override
    public void onTaskCancelled(String data) {

    }


    @Override
    public void onTaskComplete(String result, String tag) {


        //Log.e("results", result);
        if (result.equalsIgnoreCase("fail")) {

            ShowSnackBar();

//            Toast.makeText(getApplicationContext(), "please check your internet connection and try again!!!!!!", Toast.LENGTH_LONG).show();
        } else if (tag.equalsIgnoreCase("autocompletecity")) {


            try {
                JSONObject job1 = new JSONObject(result);


                Log.e(getPackageName().getClass().getName(), " inside autocomplete");
                if (job1 != null) {
                    JSONArray jarry = job1.getJSONArray("predictions");

                    cities = new ArrayList();

                    for (int i = 0; i < jarry.length(); i++) {
                        JSONObject jo = jarry.getJSONObject(i);
                        cities.add(jo.getString("description"));
                        city_place_id = jo.getString("place_id");
                    }

                    //Log.e(getPackageName().getClass().getName().toString(), cities.toString());
                    adpt = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, cities) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;

                        }

                    };

                    autoselectcity.setAdapter(adpt);
                    String laturl = "https://maps.googleapis.com/maps/api/place/details/json?";
                    para_lat = new HashMap<>();
                    para_lat.put("placeid", city_place_id);
                    para_lat.put("key", browserKey);
                    HttpAsync lat = new HttpAsync(getApplicationContext(), listener, laturl, para_lat, 1, "city_latitude");
                    lat.execute();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("city_latitude")) {
            try {
                JSONObject job1 = new JSONObject(result);
                if (job1 != null) {
                    JSONObject job2 = job1.getJSONObject("result");
//                    if(jsonArray.length()>0){
//                        JSONObject jsonObject = job2.getJSONObject(3);
//                        String add = jsonObject.getString("formatted_address");
//                        Log.e("add",add);
                    JSONObject jsonObject1 = job2.getJSONObject("geometry");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                    lati = Double.valueOf(jsonObject2.getString("lat"));
                    longi = Double.valueOf(jsonObject2.getString("lng"));
                    SharedPreferences.Editor edt = locationPref.edit();
                    edt.putString("lat", String.valueOf(lati));
                    edt.putString("long", String.valueOf(longi));
                    edt.commit();
//                        Log.e("latlng", String.valueOf(latitude)+","+String.valueOf(longitude));
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (tag.equalsIgnoreCase("register")) {

            progressDialog.dismiss();

            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    //Log.e("success", job.getBoolean("success") + " ");
                    //Log.e("msg", job.getString("msg"));
                    if (job.optBoolean("success", true)) {
//                        SharedPreferences.Editor edt = mpref.edit();
//                        edt.putBoolean("status", true);
//                        edt.putString("type", job.optString("user_type"));
//                        edt.apply();
                        setResult(RESULT_OK);
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HotelierRegistration.this, Verify.class);
                        intent.putExtra("auth_token", Authtoken);
                        intent.putExtra("registration", "hotel_reg");
                        intent.putExtra("emailid", emailText.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//            else if (tag.equalsIgnoreCase("autocompletehotel")) {
//                try {
//                    JSONObject job1 = new JSONObject(result);
//                    if (job1 != null) {
//                        JSONArray jarry = job1.getJSONArray("predictions");
//
//                        cities = new ArrayList();
//
//                        for (int i = 0; i < jarry.length(); i++) {
//                            JSONObject jo = jarry.getJSONObject(i);
//                            cities.add(jo.getString("description"));
//                        }
//
//                        adpt = new ArrayAdapter<String>(HotelierRegistration.this,
//                                android.R.layout.simple_list_item_1, cities) {
//
//                            @Override
//                            public View getView(int position, View convertView, ViewGroup parent) {
//                                View view = super.getView(position, convertView, parent);
//                                TextView text = (TextView) view.findViewById(android.R.id.text1);
//                                text.setTextColor(Color.BLACK);
//                                return view;
//                            }
//                        };
//
//                        autoselecthotel.setAdapter(adpt);
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//        } else if (tag.equalsIgnoreCase("maps")) {
//            Toast.makeText(getApplicationContext(), "coming here", Toast.LENGTH_LONG).show();
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                if (jsonObject != null) {
//                    JSONArray jsonArray = jsonObject.getJSONArray("results");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
//                        JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
//                        lati = jsonObject3.getDouble("lat");
//                        Log.e("lat", lati.toString());
//                        longi = jsonObject3.getDouble("lng");
//                        googleMap.clear();
//
//                        googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(lati, longi))
//                                .title(autoselecthotel.getText().toString())
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                        final CameraPosition cameraPosition = new CameraPosition.Builder()
//                                .target(new LatLng(lati, longi))      // Sets the center of the map to Mountain View
//                                .zoom(15)                   // Sets the zoom
//                                .bearing(90)                // Sets the orientation of the camera to east
//                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                                .build();
//                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    }
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }


    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}