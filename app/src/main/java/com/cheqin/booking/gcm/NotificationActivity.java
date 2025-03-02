package com.cheqin.booking.gcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.cheqin.booking.Adapter.NotificationListAdapter;
import com.cheqin.booking.Bean.NotificationBean;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.cheqin.booking.R;
import com.cheqin.booking.Adapter.NotificationListAdapter;
import com.cheqin.booking.Bean.NotificationBean;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.SettingSharedPrefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NotificationActivity extends AppCompatActivity {


    ListView listView;
    Toolbar toolbar;
    TextView tv_status;
    Switch switch_gcm_disable_btn;

TextView gcm_text;

    private HashMap<String, String> parameter = null;
    private SettingSharedPrefs ss;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    NotifySharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        initToolbar();
        initSwitch();
        listviewOperation();
    }

    private void init() {

        sharedPreference = new NotifySharedPreference();
        ss = SettingSharedPrefs.getInstance(NotificationActivity.this);
    }

    private void initSwitch() {

        gcm_text = (TextView) findViewById(R.id.gcm_text);

        if(!ss.getPRE_GCM_MSG_d_status()){
            gcm_text.setText("Disable Notification");
        }else{
            gcm_text.setText("Enable Notification");
        }
        switch_gcm_disable_btn = (Switch) findViewById(R.id.switch_gcm_disable_btn);
        switch_gcm_disable_btn.setChecked(!ss.getPRE_GCM_MSG_d_status());
        switch_gcm_disable_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (Common.isNetworkAvailable(NotificationActivity.this)) {
                    if (isChecked) {
                      //  Common.showToast(NotificationActivity.this, "Checked");
                        gcmWork();
                        gcm_text.setText("Disable Notification");
                    } else {
                       // Common.showToast(NotificationActivity.this, "unChecked");
                        disableGcmMessage();
                        gcm_text.setText("Enable Notification");
                    }
                } else {
                    switch_gcm_disable_btn.setChecked(!isChecked);
                    Toast.makeText(NotificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification");
    }

    private void listviewOperation() {
        listView = (ListView) findViewById(R.id.list);
        tv_status = (TextView) findViewById(R.id.tv_status);


        ArrayList<NotificationBean> values = sharedPreference.getFavorites(NotificationActivity.this);
        if (values != null && values.size() > 0) {
            NotificationListAdapter adapter = new NotificationListAdapter(NotificationActivity.this, values);
            listView.setAdapter(adapter);
        } else {
            listView.setVisibility(View.INVISIBLE);
            tv_status.setVisibility(View.VISIBLE);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_notification, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private void disableGcmMessage() {
        parameter = new HashMap<>();
        parameter.put("uid", ss.getPRE_USER_PROFILE_id());
        //Log.e("para_register", parameter.toString());

        HttpAsync httpAsync = new HttpAsync(NotificationActivity.this, new AsyncTaskListener() {

            @Override
            public void onTaskCancelled(String data) {
                Toast.makeText(NotificationActivity.this, getResources().getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTaskComplete(String result, String tag) {
                //Log.e("response", result);
                if (result.equalsIgnoreCase(Constants.FAIL)) {
                    Toast.makeText(NotificationActivity.this, "internet is not available", Toast.LENGTH_SHORT).show();
                } else {
                    parseJsonOfDeleteMessage(result);
                }

            }
        }, Constants.BASE_URL + Constants.GCM_DELETE, parameter, 2, null);
        //Constants.BASE_URL + Constants.PURCHASED_DEALS_URL
        httpAsync.execute();
    }

    private void parseJsonOfDeleteMessage(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                boolean boo = jsonObject.getBoolean("success");
               // Log.e("boolean", boo + "");
                if (boo) {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    ss.setPRE_GCM_MSG_d_status(true);
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg") + "", Toast.LENGTH_SHORT).show();
                    ss.setPRE_GCM_MSG_d_status(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void gcmWork() {
        if (Common.isNetworkAvailable(NotificationActivity.this)) {

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.

                Intent intent = new Intent(this, RegistrationIntentService.class);
                intent.putExtra("fromNotificationActivity",true);
                startService(intent);

            }

        } else {
            Toast.makeText(getApplicationContext(), "Gcm Operation is Not Performed due To Network Problem", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
        }
//        else if (id == R.id.action_clear) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}
