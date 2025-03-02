package com.cheqin.booking.Log;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by user on 09-02-2016.
 */
public class PreForgotPassword extends AppCompatActivity implements AsyncTaskListener {

    private Progressloadingdialog pDialog = null;
    private HashMap<String, String> passwdPara = null;
    private EditText edt_email = null;
    private Button btn_submit = null;
    private AsyncTaskListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        edt_email = (EditText) findViewById(R.id.edt_email);
        btn_submit = (Button) findViewById(R.id.btn_submit_email);

        listener = PreForgotPassword.this;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_email.getText().toString().equalsIgnoreCase("")){
                    //btn_submit.setBackgroundResource(R.drawable.button_unselect);
                    btn_submit.setClickable(false);
                }else {
                    //btn_submit.setBackgroundResource(R.drawable.button_s);
                    btn_submit.setClickable(true);
                }
            }
        };

        edt_email.addTextChangedListener(textWatcher);

        edt_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!edt_email.getText().toString().equalsIgnoreCase("")){
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        checkConnection();
                    }

                }else
                {
                    btn_submit.setClickable(false);
                }
                return true;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_email.getText().toString().equalsIgnoreCase("")){
//                    btn_submit.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                    checkConnection();
                }else
                {
                    btn_submit.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                }

            }
        });

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
        pDialog = new Progressloadingdialog(PreForgotPassword.this, "Sending Verification Code");
        pDialog.setCancelable(false);
        pDialog.show();
        passwdPara = new HashMap<String, String>();
        passwdPara.put("email", edt_email.getText().toString());
        HttpAsync pass = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.FORGOT, passwdPara, 2, "forgotpass");
        pass.execute();
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
        pDialog.dismiss();
        if (result.equalsIgnoreCase("fail")) {
            ShowSnackBar();
        } else if (tag.equalsIgnoreCase("forgotpass")) {

            try {
                JSONObject job = new JSONObject(result);
                if (job != null && !job.get("success").equals("error")) {
                   // Log.e("success", job.getString("success") + " ");
                    //Log.e("msg", job.getString("msg"));
                    if (job.optBoolean("success", true)) {

                        Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                        intent.putExtra("emailid", edt_email.getText().toString());
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
