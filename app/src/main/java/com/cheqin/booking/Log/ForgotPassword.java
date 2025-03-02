package com.cheqin.booking.Log;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by user on 06-11-2015.
 */
public class ForgotPassword extends AppCompatActivity implements AsyncTaskListener{
    private static ForgotPassword inst;
    private String email = null;
    private HashMap<String, String> para = null;
    private AsyncTaskListener listener1 = null;
    private EditText verif=null;
    private Button verifying=null;
    private TextView txt_email = null;
    private Progressloadingdialog progressDialog = null;
    private String URL = " http://api.mypillows.company/my_deals/check_pass_code_verf/verify.json?";
    public static ForgotPassword instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Enter the Code");

        txt_email = (TextView) findViewById(R.id.txt_email);
        verif = (EditText) findViewById(R.id.code);
        verifying = (Button) findViewById(R.id.button4);
        Intent intent = getIntent();
        email = intent.getStringExtra("emailid");
        txt_email.setText(email);

        listener1 = ForgotPassword.this;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (verif.length() == 4) {
                    verifying.setBackgroundResource(R.drawable.btn_login_bg);
                    verifying.setClickable(true);

                } else {
                    verifying.setBackgroundResource(R.drawable.button_unselect);
                    verifying.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        verif.addTextChangedListener(textWatcher);

        verif.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!(verif.length() == 4)) {
//                    btn_submit.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                    checkConnection();
                } else {
                    verifying.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                }
                return true;
            }
        });

        verifying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(verif.length() == 6)) {
//                    btn_submit.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                    checkConnection();
                } else {
                    verifying.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
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

    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {
            sendverification();
        } else {
            //Toast.makeText(getApplicationContext(),"Internet is not Avilable",Toast.LENGTH_SHORT).show();
            ShowSnackBar();
        }
    }

    private void sendverification() {
        progressDialog = new Progressloadingdialog(ForgotPassword.this, "Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        para = new HashMap<>();
        para.put("email",txt_email.getText().toString());
        para.put("scode",verif.getText().toString());
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener1, URL, para, 2, "verify");
        httpAsync.execute();
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
    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        if (tag.equalsIgnoreCase("verify")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    //Log.e("success", job.getBoolean("success") + " ");
                    //Log.e("msg", job.getString("msg"));
                    if (job.optBoolean("success", true)) {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPassword.this, NewPassword.class);
                        intent.putExtra("emailId",email);
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
        progressDialog.dismiss();
    }


   }

