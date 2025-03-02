/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cheqin.booking.gcm;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    Context context;
    SharedPreferences sharedPreferences;
    SettingSharedPrefs ssp;
    HashMap<String, String> params = null;
    boolean fromNotificationActivity = false;


    public RegistrationIntentService() {
        super(TAG);
        context = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        fromNotificationActivity = intent.getBooleanExtra("fromNotificationActivity", false);
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
          //comm  InstanceID instanceID = InstanceID.getInstance(this);
            //getString(R.string.gcm_defaultSenderId)
            //comm   String token = instanceID.getToken("134893042610", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            421981303756
            // [END get_token]
            //comm   Log.e(TAG, "GCM Registration Token: " + token);
//            if(sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER,false) == false) {
            // TODO: Implement this method to send any registration to your app's servers.
            //comm  sendRegistrationToServer(token);
//            }else{
//                Log.e("token","token already stored in server");
//            }
            // Subscribe to topic channels
            //comm   subscribeTopics(token);
//            // You should store a boolean that indicates whether the generated token has been
//            // sent to your server. If the boolean is false, send the token to your server,
//            // otherwise your server should have already received the token.
//            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
//            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        //Log.e("ToServer", "sendRegistrati");
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
       // Log.e("uid", ssp.getPRE_USER_PROFILE_id());
        params = new HashMap<>();
        params.put("gcm_regid", token);
        params.put("uid", ssp.getPRE_USER_PROFILE_id());
        params.put("email", ssp.getPRE_USER_PROFILE_email());
        params.put("device_type", "ANDROID");
        new HttpAsync(this, new AsyncTaskListener() {
            @Override
            public void onTaskCancelled(String data) {
                // cancelProgressBar();
                Log.e("onTaskCancelled", data);
            }

            @Override
            public void onTaskComplete(String result, String tag) {
                //Log.e("onTaskComplete", result);
                JSONObject job = null;
                try {
                    job = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equalsIgnoreCase(Constants.FAIL)) {
                    Toast.makeText(getApplicationContext(), "Gcm Operation is Not Performed due To Network Problem", Toast.LENGTH_SHORT).show();
                } else {
                    if (job != null) {
                        try {

                            if (job.getBoolean("success")) {
                                if (fromNotificationActivity) {
                                    Toast.makeText(getApplicationContext(), job.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                                SettingSharedPrefs.getInstance(RegistrationIntentService.this).setPRE_GCM_MSG_d_status(false);
                            } else {
                                Toast.makeText(getApplicationContext(), job.getString("msg"), Toast.LENGTH_SHORT).show();
//                                SettingSharedPrefs.getInstance(RegistrationIntentService.this).setPRE_GCM_MSG_d_status(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // You should store a boolean that indicates whether the generated token has been
                    // sent to your server. If the boolean is false, send the token to your server,
                    // otherwise your server should have already received the token.
                    sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                    // [END register_for_gcm]
                }
            }

        }," http://api.mypillows.company/my_deals/create_updategcm/update.json?",params,1,null).

            execute();
        }

                // http://localhost/MyDealsGcm/REGISTER.PHP?regId=dbri6l3gmu8%3AAPA91bHNF8FhSmxoJZhNgcFWUoGMYK2wxSOOiGPwvuxo2qpmiqXtrnzW6VkRmnq32BqP6TqXIH1-GDxQocdWnvwR5KoxZD-mCYVoDHGdB6vEU5dTKbAC5sCYZahUeLwGDhjfsjVw55Nk
                // http://localhost/MyDealsGcm/REGISTER.PHP?regId=dbri6l3gmu8%3AAPA91bHNF8FhSmxoJZhNgcFWUoGMYK2wxSOOiGPwvuxo2qpmiqXtrnzW6VkRmnq32BqP6TqXIH1-GDxQocdWnvwR5KoxZD-mCYVoDHGdB6vEU5dTKbAC5sCYZahUeLwGDhjfsjVw55Nk

                /**
                 * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
                 *
                 * @param token GCM token
                 * @throws IOException if unable to reach the GCM PubSub service
                 */
                // [START subscribe_topics]

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]


    ProgressDialog pd = null;

    private void showProgress() {
        pd = new ProgressDialog(context);
        pd.setMessage("Storing Token...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    private void cancelProgressBar() {
        if (pd != null) {
            pd.cancel();
            pd = null;
        }

    }


}
