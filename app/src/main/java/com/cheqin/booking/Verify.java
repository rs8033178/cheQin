package com.cheqin.booking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
//import android.util.Log;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Log.ForgotPassword;
import com.cheqin.booking.Log.HotelierAddDetails;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.User.UserLiveRequests;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.CustomFontTextview;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Verify extends AppCompatActivity implements AsyncTaskListener {
    private static Verify inst;
    private String email = null;
    private String Auth_token = null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener = null;
    private EditText verif=null;
    private Button verifying=null;
    private Progressloadingdialog progressDialog = null;
    Intent intent;
    SettingSharedPrefs ssp;
//    private SharedPreferences shared_verify = null;
    private HashMap<String, String> postParameter = null;
    private boolean isPostOffer = false;
    private Bundle bundle = null;
CustomFontTextview mobileNumberTxt;
String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Verifying your Account");

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
        verif = (EditText) findViewById(R.id.verify_code);
        verifying = (Button) findViewById(R.id.verifybutton);
        intent = getIntent();
        email = intent.getStringExtra("emailid");
        mobile = intent.getStringExtra("mobile");
        Auth_token = intent.getStringExtra("auth_token");
//        Registration = intent.getStringExtra("registration");

//        shared_verify = getSharedPreferences("verify",MODE_PRIVATE);
        mobileNumberTxt=(CustomFontTextview)findViewById(R.id.mobileNumberTxt) ;
        if(mobile!=null) {
            mobileNumberTxt.setText(mobileNumberTxt.getText().toString() + " " + mobile);
        }

        postParameter = UserBooking.para1;
        bundle = getIntent().getExtras();
        if (bundle != null) {
            isPostOffer = bundle.getBoolean("post");
        }

        listener=Verify.this;
        verifying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verif.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(Verify.this, "Enter the Verification code", Toast.LENGTH_LONG).show();
                }
                else {
                    checkConnectionAndRegistration();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkConnectionAndRegistration() {
        if (Common.isNetworkAvailable(getApplicationContext())){
            doverify();
        }else{
            //Toast.makeText(getApplicationContext(),"Internet is not Avilable",Toast.LENGTH_SHORT).show();
            ShowSnackBar();
        }
    }


    private void doverify() {

        progressDialog = new Progressloadingdialog(Verify.this,"Verifying..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        para = new HashMap<>();
        para.put("verif_email", email);
        para.put("email_code",verif.getText().toString());
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.VERIFY , para, 2, "verify");
        httpAsync.execute();

    }

    private void ShowSnackBar() {
        String styledText = "<font color='black'>No Internet Connection</font>.";
        // msnackBar;
        Snackbar snackbar= Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnectionAndRegistration();
                    }
                })
                .setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#ffffff")); // snackbar background color
        // snackbar.setActionTextColor(Color.parseColor("#000000")); // snackbar action text color
        snackbar.show();
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        if(result.equalsIgnoreCase("fail"))
        {
            ShowSnackBar();
        }

        else if (tag.equalsIgnoreCase("verify")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                  //  Log.e("success", job.getBoolean("success") + " ");
                    //Log.e("msg", job.getString("msg"));
                    if (job.optBoolean("success", true)) {
//                        SharedPreferences.Editor verify = shared_verify.edit();
//                        verify.putBoolean("verify", true);
//                        verify.commit();

//                        ssp.setPRE_auth_token(Auth_token);


                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                        if(job.getString("user_type").equalsIgnoreCase("buyer"))
                        {
//                            setResult(RESULT_OK);
//                            finish();

                            ssp.setPRE_user_login(true);
                            ssp.setPRE_hotelier_login(false);
                            ssp.setPRE_usertype(job.getString("user_type"));
                            ssp.setPRE_auth_token(Auth_token);
//                            if (isPostOffer){
                                if (isPostOffer) {
//                                    Toast.makeText(getApplicationContext(), "buyer", Toast.LENGTH_SHORT).show();

                                    String URL1 = " http://api.mypillows.company/user_requests/create/new.json?";
                                    UserBooking.para1.put("auth_token", ssp.getPRE_auth_token());
                                    HttpAsync user_values = new HttpAsync(getApplicationContext(), listener, URL1, postParameter, 2, "user_values");
                                    user_values.execute();
                                    Intent intent=new Intent(getApplicationContext(),UserLiveRequests.class);
                                    intent.putExtra("auth_token",ssp.getPRE_auth_token());
                                    startActivity(intent);
                                    finish();
                                } else {
//                                    Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
//                                    ssp.setPRE_user_login(true);
//                                    ssp.setPRE_hotelier_login(false);
//                                    ssp.setPRE_usertype(job.getString("user_type"));
//                                    ssp.setPRE_auth_token(Auth_token);
//                                    Log.e("authhhhhhhh",Auth_token);
                                    Intent intent=new Intent(getApplicationContext(),UserBooking.class);
                                    intent.putExtra("auth_token",ssp.getPRE_auth_token());
                                    startActivity(intent);
                                    finish();
                                }
//                            }

                        }
                        else if(job.getString("user_type").equalsIgnoreCase("buysell"))
                        {
                            ssp.setPRE_auth_token(Auth_token);
                            Intent intent1_hotel = new Intent(Verify.this, HotelierAddDetails.class);
                            intent1_hotel.putExtra("auth_token",ssp.getPRE_auth_token());
                            intent1_hotel.putExtra("emailid", email);
                            startActivity(intent1_hotel);
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }

        }
        progressDialog.dismiss();
    }


    }

