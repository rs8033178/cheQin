package com.cheqin.booking.Log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cheqin.booking.R;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by user on 14-01-2016.
 */
public class SocialRegistration extends AppCompatActivity implements AsyncTaskListener{

    private SweetAlertDialog pDialog = null;
    private HashMap<String,String> para = null;
    private AsyncTaskListener listener = null;
    private EditText first_name;
    private EditText last_name;
    private EditText emailText;
    private EditText phone;
    private Button glogin;
    private AutoCompleteTextView autoselectcity = null;
    private SharedPreferences locationPref = null;
    private Double latitude = null;
    private Double longitude = null;
    SettingSharedPrefs ssp;
    private String randompass="";
    String password = null;
    private String usertype = "buyer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_reg);

        listener = SocialRegistration.this;
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
        first_name = (EditText) findViewById(R.id.f_name);
        last_name = (EditText) findViewById(R.id.l_name);
        emailText = (EditText) findViewById(R.id.input_email);
        phone = (EditText) findViewById(R.id.input_phone);
        autoselectcity = (AutoCompleteTextView) findViewById(R.id.city);
        glogin = (Button) findViewById(R.id.btn_gsignup);

        locationPref = getSharedPreferences("latlong", MODE_PRIVATE);

        if (locationPref != null) {
            latitude = Double.valueOf(locationPref.getString("lat", null));
            longitude = Double.valueOf(locationPref.getString("long", null));
        }

//        gintent = getIntent();
//        first_name.setText(gintent.getStringExtra("name"));
//        emailText.setText(gintent.getStringExtra("email"));

        Intent gplus_details = getIntent();
        first_name.setText(gplus_details.getStringExtra("name"));
//        Pass = gplus_details.getStringExtra("auth_token");
        emailText.setText(gplus_details.getStringExtra("email"));
        randompass = random();
        password=GenerateAuthtoken.encryptpassregistration(randompass);

        glogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_name.getText().toString().equalsIgnoreCase("") ) {
                    first_name.setError("Enter at least 3 characters");
                } else if (last_name.getText().toString().equalsIgnoreCase("")) {
                    last_name.setError("Enter at least 2 characters");
                } else if (emailText.getText().toString().equalsIgnoreCase("")) {
                    emailText.setError("enter a valid email address");
                }
                else if (phone.getText().toString().equalsIgnoreCase("")) {
                    phone.setError("between 0 and 9 numbers");
                } else if (autoselectcity.getText().toString().equalsIgnoreCase("")) {
                    autoselectcity.setError("Select City");
                } else {

                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        dogooglelogin();
//                        startActivity(new Intent(getApplicationContext(),UserBooking.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet is not Avilable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public static String random() {
        int MAX_LENGTH=8;
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghikllmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( MAX_LENGTH );
        for( int i = 0; i < MAX_LENGTH; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private void dogooglelogin() {

//        Authtoken = GenerateAuthtoken.generate(emailText.getText().toString(), Pass, "gmail");
//
//        SharedPreferences.Editor logout = mpref.edit();
//        logout.putString("auth", Authtoken);
//        logout.commit();



//        Log.e("auth token is ", auth_token);
        /*pDialog = new SweetAlertDialog(SocialRegistration.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();*/

        para = new HashMap<>();
        para.put("user[fname]", first_name.getText().toString());
        para.put("user[lname]", last_name.getText().toString());
        para.put("user[email]", emailText.getText().toString());
        para.put("user[password]", password);
        para.put("address[mobile]", phone.getText().toString());
        para.put("user_type", "buyer");
        para.put("address[city]", autoselectcity.getText().toString());
        para.put("cityLatLong", latitude + "||" + longitude);
        para.put("partnerId", "");
        para.put("id","new");
//        para.put("auth_token", auth_token);
        HttpAsync tokenAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "/users/create_guest_social/new.json?", para, 2, "google_login");
        tokenAsync.execute();

    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        if (tag.equalsIgnoreCase("google_login")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    //Log.e("success", job.getBoolean("success") + " ");
                    //Log.e("msg", job.getString("msg"));
                    if (job.optBoolean("success", true)) {

                        String encrypted_value=GenerateAuthtoken.generate(emailText.getText().toString(), randompass, "social");

                        ssp.setPRE_user_login(true);
                        ssp.setPRE_hotelier_login(false);
                        ssp.setPRE_auth_token(encrypted_value);
                        ssp.setPRE_usertype(usertype);

                        Intent intent = new Intent(getApplicationContext(), UserBooking.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pDialog.dismiss();

    }
}
