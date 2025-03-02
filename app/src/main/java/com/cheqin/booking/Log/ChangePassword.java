package com.cheqin.booking.Log;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cheqin.booking.LoginActivity;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChangePassword extends AppCompatActivity implements AsyncTaskListener {

    private AsyncTaskListener listener = null;
    private Progressloadingdialog pd = null;
    private HashMap<String, String> npass = null;
    private EditText edt_newpasswd = null;
    private EditText edt_oldpass = null;
    private EditText edt_confirmpass = null;
    private String Url = " http://api.mypillows.company/users/update_password/change_password.json?";
    String auth_token = null;
    private Button btn_change = null;
    private String email = null;
    private String old_password = null;

    SettingSharedPrefs ssp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        listener = ChangePassword.this;
        Intent i = getIntent();
        auth_token = i.getStringExtra("auth_token");
        email = i.getStringExtra("emailid");
        old_password = i.getStringExtra("oldpass");

        edt_newpasswd = (EditText) findViewById(R.id.edt_newpasswd);
        edt_oldpass = (EditText) findViewById(R.id.edt_oldpasswd);
        edt_confirmpass = (EditText) findViewById(R.id.edt_confirmnewpasswd);

        btn_change = (Button) findViewById(R.id.button4);

        auth_token = ssp.getPRE_auth_token();


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (old_password.equalsIgnoreCase(edt_oldpass.getText().toString())) {

                    if (edt_newpasswd.getText().toString().equalsIgnoreCase(edt_confirmpass.getText().toString())) {
                        npass = new HashMap<String, String>();
                        npass.put("auth_token", auth_token);
                        String password = GenerateAuthtoken.encryptpassregistration(edt_newpasswd.getText().toString());
                        npass.put("passwd", password);
                        pd = new Progressloadingdialog(ChangePassword.this, "Please wait...");
                        pd.setCancelable(false);
                        pd.show();
                        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Url, npass, 2, "reset");
                        httpAsync.execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Confirm password doesnot match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Old password doesnot match", Toast.LENGTH_LONG).show();
                }
            }
        });

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
                    //Log.e("success", jsonObject.getBoolean("success") + "");
                    //Log.e("msg", jsonObject.getString("msg"));
                    if (jsonObject.optBoolean("success", true)) {
                        Toast.makeText(getApplicationContext(), jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        ssp.clearSharedPreference();
                        auth_token = GenerateAuthtoken.generate(email, edt_newpasswd.getText().toString(), "normal");

                        ssp.setPRE_auth_token(auth_token);
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
