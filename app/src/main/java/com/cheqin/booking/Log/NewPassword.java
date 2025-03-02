package com.cheqin.booking.Log;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.LoginActivity;
import com.cheqin.booking.MainActivity;
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
 * Created by user on 06-11-2015.
 */
public class NewPassword extends AppCompatActivity implements AsyncTaskListener {

    private EditText edt_passwd = null;
    private String np_email = null;
    private Button btn_proceed = null;
    private AsyncTaskListener listener = null;
    private ProgressDialog pd = null;
    private ImageView pass_visible = null;
    private ImageView pass_invisible = null;
    private HashMap<String, String> npass = null;
    private String Url = " http://api.mypillows.company/my_deals/change_pass/reset.json?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_passwd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Reset your Password");
        listener = NewPassword.this;
        Intent i = getIntent();
        np_email = i.getStringExtra("emailId");
        pass_visible = (ImageView) findViewById(R.id.btn_visible);
        pass_invisible = (ImageView) findViewById(R.id.btn_invisible);
        edt_passwd = (EditText) findViewById(R.id.input_passwd);
        btn_proceed = (Button) findViewById(R.id.button4);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt_passwd.getText().toString().equalsIgnoreCase("")){
                    btn_proceed.setBackgroundResource(R.drawable.button_unselect);
                    btn_proceed.setClickable(false);
                }else {
                    btn_proceed.setBackgroundResource(R.drawable.btn_login_bg);
                    btn_proceed.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        edt_passwd.addTextChangedListener(textWatcher);

        pass_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                pass_visible.setVisibility(View.GONE);
                pass_invisible.setVisibility(View.VISIBLE);
            }
        });

        pass_invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                pass_invisible.setVisibility(View.GONE);
                pass_visible.setVisibility(View.VISIBLE);
            }
        });

        edt_passwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!edt_passwd.getText().toString().equalsIgnoreCase("")){
//                    btn_submit.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                    checkConnection();
                }else
                {
                    btn_proceed.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                }

                return true;
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_passwd.getText().toString().equalsIgnoreCase("")){
//                    btn_submit.setClickable(false);
//                    btn_submit.setBackgroundResource(R.color.gray_btn_bg_color);
                    checkConnection();
                }else
                {
                    btn_proceed.setClickable(false);
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
        npass = new HashMap<String, String>();
        npass.put("email", np_email);
        String password =  GenerateAuthtoken.encryptpassregistration(edt_passwd.getText().toString());
        npass.put("pass", password);
        pd = new ProgressDialog(NewPassword.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Url, npass, 2, "reset");
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

        if (tag.equalsIgnoreCase("reset")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    //android.util.Log.e("success", jsonObject.getBoolean("success") + "");
                   // android.util.Log.e("msg", jsonObject.getString("msg"));
                    if (jsonObject.optBoolean("success", true)) {
                        Toast.makeText(getApplicationContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        pd.dismiss();

    }
}
