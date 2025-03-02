package com.cheqin.booking.User;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by user on 06-11-2015.
 */
public class UserFeedback extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_feedback_type;
    RadioGroup radioGroup;
    int checkedRId=-1;
    Button btn_send;
    EditText ed_wyf;
    Progressloadingdialog progressDialog;
    private HashMap<String, String> parameter = null;

    SettingSharedPrefs ssp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        initToolbar();
        init();
        ed_wyf = (EditText) findViewById(R.id.ed_wyf);
        initFeedBackTypeButton();
        initSendButton();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new Progressloadingdialog(UserFeedback.this,"sending..");

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("FeedBack");
    }



    private void initFeedBackTypeButton() {
        btn_feedback_type = (Button) findViewById(R.id.edt_feedback_type);

        // add button listener
        btn_feedback_type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(UserFeedback.this);
                dialog.setContentView(R.layout.dialog_feedback_type);
                dialog.setTitle("Select Feedback Type");

                final RadioButton but = (RadioButton) dialog.findViewById(R.id.radioButton);
                final RadioButton but1 = (RadioButton) dialog.findViewById(R.id.radioButton1);
                final RadioButton but2 = (RadioButton) dialog.findViewById(R.id.radioButton2);
                final RadioButton but3 = (RadioButton) dialog.findViewById(R.id.radioButton3);
                // RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rBGPoly);

                radioGroup = (RadioGroup) dialog.findViewById(R.id.rBGPoly);
                radioGroup.check(checkedRId);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // find which radio button is selected
                        checkedRId = checkedId;
                        if (checkedId == R.id.radioButton) {
                            btn_feedback_type.setText(but.getText().toString().trim());
                            dialog.dismiss();
                        } else if (checkedId == R.id.radioButton1) {
                            btn_feedback_type.setText(but1.getText().toString().trim());
                            dialog.dismiss();
                        } else if (checkedId == R.id.radioButton2) {
                            btn_feedback_type.setText(but2.getText().toString().trim());
                            dialog.dismiss();
                        } else if (checkedId == R.id.radioButton3) {
                            btn_feedback_type.setText(but3.getText().toString().trim());
                            dialog.dismiss();
                        }
                    }

                });
                dialog.show();
            }
        });


    }

    private void initSendButton() {
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSnakBar();
                doVerify();
            }
        });
    }

    private void doVerify() {

        if (btn_feedback_type.getText().toString().trim().equalsIgnoreCase("Feedback Type")) {
            Toast.makeText(getApplicationContext(), "Select Feedback Type First", Toast.LENGTH_SHORT).show();
        }else if (ed_wyf.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Write Your Feedback First", Toast.LENGTH_SHORT).show();
        }else {
            checkInternetConnection();
        }
    }

    private void checkInternetConnection() {

        if (Common.isNetworkAvailable(getApplicationContext())) {
            doFeedback();
        } else {
            //  Toast.makeText(getApplicationContext(), "Internet is not Avilable", Toast.LENGTH_SHORT).show();
            ShowSnackBar();
        }
    }
    Snackbar snackbar;
    private void ShowSnackBar() {
        String styledText = "<font color='black'>No Internet Connection</font>.";
        // msnackBar;
        snackbar= Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkInternetConnection();
                    }
                })
                .setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#ffffff")); // snackbar background color
        //snackbar.setActionTextColor(Color.parseColor("#000000")); // snackbar action text color
        snackbar.show();
    }

    private void doFeedback() {

        parameter = new HashMap<>();
        parameter.put("auth_token",ssp.getPRE_auth_token());
        parameter.put("feedback_type",btn_feedback_type.getText().toString().trim());
        Log.e("feedback_type",btn_feedback_type.getText().toString().trim() + " d");
        parameter.put("feedback_text",ed_wyf.getText().toString().trim());

        Log.e("para_register", parameter.toString());
        progressDialog.setCancelable(false);
        progressDialog.show();
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), new AsyncTaskListener() {
            @Override
            public void onTaskCancelled(String data) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTaskComplete(String result, String tag) {
                Log.e("response :: ", result);
                progressDialog.dismiss();

                if(result.equalsIgnoreCase(Constants.FAIL)){
                    // Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    ShowSnackBar();
                }
                else {
                    parseJson(result);
                }

            }
        }, Constants.BASE_URL + Constants.FEEDBACK, parameter, 2, null);
        httpAsync.execute();

    }


    private void parseJson(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                boolean boo = jsonObject.getBoolean("success");
                Log.e("boolean", boo + "");
                if (boo) {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),jsonObject.getString("msg")+"",Toast.LENGTH_SHORT).show();
                    //sucess False
//                    checkAuthTokenValid(jsonObject.getString("msg"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void checkAuthTokenValid(String msg) {
        if (msg.equalsIgnoreCase(Constants.INVALID_EMAIL_PASS)) {
            Common.doLogoutOperation(this);
        } else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }





//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_feed_back, menu);
//        return true;
//    }


    private void hideSnakBar() {
        if (snackbar != null ){
            snackbar.dismiss();
        }
    }

}
