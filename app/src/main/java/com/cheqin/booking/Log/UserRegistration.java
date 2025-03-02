package com.cheqin.booking.Log;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.R;
import com.cheqin.booking.Verify;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class UserRegistration extends AppCompatActivity implements AsyncTaskListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private EditText first_name;
    private EditText last_name;
    private EditText emailText;
    private EditText passwordText;
    protected TextView nonmembr;
    private EditText phone;
    private AutoCompleteTextView autoselectcity = null;
    private Button signupButton;
    private TextView loginLink;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    private Progressloadingdialog progressDialog = null;
    private SharedPreferences locationPref = null;

    private int citySelected = 0;
    private String text = "";
    private static final String TAG_RESULT = "predictions";
    private HashMap<String, String> parameter = null;
    private String browserKey ;
    private List<String> cities = null;
    private ArrayAdapter<String> adpt = null;
    private String city = "";
    private Double latitud = null;
    private Double longitud = null;

    private HashMap<String, String> postParameter = null;
    private boolean isPostOffer = false;
    private Bundle bundle = null;
    private String passwd =null;
//    private GoogleApiClient mGoogleApiClient;
  //  PlaceArrayAdapter  mPlaceArrayAdapter;
    String selectedLat,selectedLng;
    String countryShortname;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("SignUp");
        browserKey=new SharedPreference().getApiKey(getApplicationContext());

        first_name = (EditText) findViewById(R.id.f_name);
        last_name = (EditText) findViewById(R.id.l_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        phone = (EditText) findViewById(R.id.input_phone);
        autoselectcity = (AutoCompleteTextView) findViewById(R.id.city);
        signupButton = (Button) findViewById(R.id.btn_signup);
        // signupButton.setEnabled(false);
        nonmembr = (TextView) findViewById(R.id.tv_signin);

        locationPref = getSharedPreferences("latlong", MODE_PRIVATE);
//        mGoogleApiClient = new mGoogleApiClient.Builder(UserRegistration.this)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(this, 0, this)
//                .addConnectionCallbacks(this)
//                .build();

        if (locationPref != null) {
            try {
                latitud = Double.valueOf(locationPref.getString("lat", null));
                longitud = Double.valueOf(locationPref.getString("long", null));
            }catch (Exception e){
                latitud = Double.valueOf(12.9716);
                longitud = Double.valueOf(77.5946);
            }

        }

        //underline
        String udata = "Already a member? Login";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
//        nonmembr.setText(content);

        listener = UserRegistration.this;

//        postParameter = UserBooking.para1;
        bundle = getIntent().getExtras();
        if (bundle != null) {
            isPostOffer = bundle.getBoolean("post");
        }

        //Log.e("bundle", isPostOffer + " ");

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
                    //  signupButton.setEnabled(false);
                    //signupButton.setBackgroundResource(R.drawable.button_unselect);
                    signupButton.setClickable(false);
                } else {
                    //  signupButton.setEnabled(true);
                    //signupButton.setBackgroundResource(R.drawable.button_s);
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
        LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                new LatLng(20.5937, 78.9629), new LatLng(20.5937, 78.9629));

          /*mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoselectcity.setAdapter(mPlaceArrayAdapter);*/
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!first_name.getText().toString().equalsIgnoreCase("")) && (!last_name.getText().toString().equalsIgnoreCase("")) &&
                        (!emailText.getText().toString().equalsIgnoreCase("")) && (!passwordText.getText().toString().equalsIgnoreCase("")) &&
                        (!phone.getText().toString().equalsIgnoreCase("")) && (!autoselectcity.getText().toString().equalsIgnoreCase(""))) {

                    //signupButton.setBackgroundResource(R.color.gray_btn_bg_color);
                    signupButton.setClickable(false);

                    hideSoftKeyboard();
//                    if (!validatefirstname(first_name.getText().toString())) {
//                        first_name.setError("Enter at least 2 characters");
//                    } else if (!validatelastname(last_name.getText().toString())) {
//                        last_name.setError("Enter at least 1 character");
                    if (!validatemail(emailText.getText().toString())) {
                        emailText.setError("enter a valid email address");
                    } else if (!validatepassword(passwordText.getText().toString())) {
                        passwordText.setError("Password should be minimum of 6 characters");
//                    } else if (!validatephone(phone.getText().toString())) {
//                        phone.setError("Enter a valid mobile number");
                    } else if (city.equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please choose the city", Toast.LENGTH_SHORT).show();
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

            }

            @Override
            public void afterTextChanged(Editable editable) {
                citySelected = 0;
                text = autoselectcity.getText().toString();
                text = text.trim();
                text.split(" ");

                //Log.e("afterTextChanged change", text);
                address(text);
                city = "";
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
                    parameter.put("types","(cities)");
                    parameter.put("radius", "500");
                    parameter.put("sensor", "true");
                    parameter.put("key", browserKey);
                    //Log.e("parameters", parameter.toString());

                    //new HttpAsync(UserRegistration.this, listener, Constants.AUTOCOMPLETE, parameter, 1, "autocomplete").execute();
                }
            }
        });
        autoselectcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* city = mPlaceArrayAdapter.getItem(position).toString();
                final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);

                final String placeId = String.valueOf(item.placeId);
//            Log.e("Selected City", "Selected: " + item.description);
                *//*PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);*//*
String city_place_id=null;
                try {
//                Log.e("Place ID", "Fetching details for ID: " + item.placeId);
                    city_place_id = item.placeId + "";
                } catch (Exception e) {
                    city_place_id = "ChIJbU60yXAWrjsR4E9-UejD3_g";
                }

               String laturl = "https://maps.googleapis.com/maps/api/place/details/json?";
                 HashMap para_lat = new HashMap<>();
                para_lat.put("placeid", city_place_id);
                para_lat.put("key", browserKey);
                HttpAsync lat = new HttpAsync(getApplicationContext(), UserRegistration.this, laturl, para_lat, 1, "city_latitude");
                lat.execute();*/
            }
        });

        nonmembr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = places -> {
        if (!places.getStatus().isSuccess()) {
            Log.e("Error", "Place query did not complete. Error: " +
                    places.getStatus().toString());
            return;
        }
        // Selecting the first object buffer.
        final Place place = places.get(0);
        CharSequence attributions = places.getAttributions();

        //            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
        //            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
        //            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
        //            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
        //            mWebTextView.setText(place.getWebsiteUri() + "");
        //            if (attributions != null) {
        //                mAttTextView.setText(Html.fromHtml(attributions.toString()));
        //            }
    };*/
    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {

//            if(Build.FINGERPRINT.startsWith("generic")
//                    || Build.FINGERPRINT.startsWith("unknown")
//                    || Build.MODEL.contains("google_sdk")
//                    || Build.MODEL.contains("Emulator")
//                    || Build.MODEL.contains("Android SDK built for x86")
//                    || Build.MANUFACTURER.contains("Genymotion")
//                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
//                    || "google_sdk".equals(Build.PRODUCT))
//            {
//                Toast.makeText(getApplicationContext(),"Registration with an emulator is restricted",Toast.LENGTH_LONG).show();
//            }
//            else
//            {
                doUserRegistration();
//            }
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


    private void doUserRegistration() {
        progressDialog = new Progressloadingdialog(UserRegistration.this, "Signing Up...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        passwd = GenerateAuthtoken.encryptpassregistration(passwordText.getText().toString());
        //Log.e("encry password", passwd);
        para = new HashMap<>();
        para.put("user[fname]", first_name.getText().toString());
        para.put("user[lname]", last_name.getText().toString());
        para.put("user[email]", emailText.getText().toString());
        para.put("user[password]", passwd);
        para.put("user[country_code]", countryShortname);
        para.put("address[mobile]", phone.getText().toString());
        para.put("user_type", "buyer");
        para.put("address[city]", autoselectcity.getText().toString());
        para.put("cityLatLong", selectedLat+"||"+selectedLng);
        para.put("partnerId", "hh");
        //Log.e("params", para.toString());
        HttpAsync tokenAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.REGISTRATION, para, 2, "user_register");
        tokenAsync.execute();
    }


    private boolean validatephone(String phoneno) {
        if (phoneno != null && phoneno.length() > 7 && phoneno.length() <= 10) {
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

        if(progressDialog!=null) {
            progressDialog.dismiss();
        }

        if (result.equalsIgnoreCase("fail")) {
            ShowSnackBar();
        } else if (tag.equalsIgnoreCase("autocomplete")) {


            try {
                JSONObject job1 = new JSONObject(result);


                //Log.e(getPackageName().getClass().getName(), " inside autocomplete");
                if (job1 != null) {
                    JSONArray jarry = job1.getJSONArray("predictions");

                    cities = new ArrayList();

                    for (int i = 0; i < jarry.length(); i++) {
                        JSONObject jo = jarry.getJSONObject(i);
                        cities.add(jo.getString("description"));
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
                    /*LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                            new LatLng(20.5937, 78.9629), new LatLng(20.5937, 78.9629));

                    PlaceArrayAdapter  mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                           BOUNDS_MOUNTAIN_VIEW, null);
                    autoselectcity.setAdapter(mPlaceArrayAdapter);*/


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("user_register")) {

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
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();

                        if (isPostOffer){

                            String Authtoken = GenerateAuthtoken.generate(emailText.getText().toString(), passwordText.getText().toString(), "normal");

                            Intent intent = new Intent(UserRegistration.this, Verify.class);
                            intent.putExtra("registration", "user_reg");
                            intent.putExtra("post", true);
                            intent.putExtra("user_type","buyer");
                            intent.putExtra("auth_token",Authtoken);
                            intent.putExtra("emailid", phone.getText().toString());
                            startActivityForResult(intent, 100);
                        }
                        else {
                            String Authtoken = GenerateAuthtoken.generate(emailText.getText().toString(), passwordText.getText().toString(), "normal");
                            Intent intent = new Intent(UserRegistration.this, Verify.class);
                            intent.putExtra("registration", "user_reg");
                            intent.putExtra("emailid", emailText.getText().toString());
                            intent.putExtra("mobile", phone.getText().toString());
                            intent.putExtra("user_type","buyer");
                            intent.putExtra("auth_token",Authtoken);
                            intent.putExtra("post", false);
                            startActivityForResult(intent, 100);
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Please SignUp", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            finish();
        }else if (tag.equalsIgnoreCase("city_latitude")) {
            try {
                JSONObject job1 = new JSONObject(result);
                if (job1 != null) {
                    JSONObject job2 = job1.getJSONObject("result");
                    JSONArray jsonArray = job2.getJSONArray("address_components");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject zero2 = jsonArray.getJSONObject(i);
                        countryShortname= zero2.getString("short_name");

                    }
                    JSONObject jsonObject1 = job2.getJSONObject("geometry");

                    JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                    selectedLat= jsonObject2.getString("lat");
                    selectedLng=jsonObject2.getString("lng");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.e("UserBooking", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        //mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("UserBooking", "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("UserRegistration", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                getResources().getString(R.string.error_generic),
                Toast.LENGTH_LONG).show();
    }
}
