package com.cheqin.booking.User;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Adapter.UserCancelBookingAdapter;
import com.cheqin.booking.Bean.Confirm_Bean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Logger;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cheqin.booking.gcm.NotificationNavigationHelperKt.OBJECT_ID;

/**
 * Created by user on 06-11-2015.
 */
public class UserCancelRequest extends AppCompatActivity implements AsyncTaskListener {

    private AsyncTaskListener asyncTaskListener = null;
    private HashMap<String, String> parameters = null;
    private Context context = null;
    private ListView lv = null;
    private Progressloadingdialog pd = null;
    private String auth_token = null;
    private List<Confirm_Bean> rowitems = null;
    private TextView nobook = null;

    SettingSharedPrefs ssp;

    private long highlightItemId = -1;
    private int scrollPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cancel_request);
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Cancelled Bookings");
        lv = (ListView) findViewById(R.id.user_cancel_list);
        nobook = (TextView) findViewById(R.id.user_no_cancel_booking);
        context = getApplicationContext();
        asyncTaskListener = UserCancelRequest.this;
        rowitems = new ArrayList<>();

        auth_token = ssp.getPRE_auth_token();

        highlightItemId = getIntent().getLongExtra(OBJECT_ID, -1);

        checkConnection();

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


        snackBarView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {

            getCancelledRequests();
//            getLocation();
        } else {
            ShowSnackBar();
        }
    }

    private void getCancelledRequests() {

        parameters = new HashMap<>();
        parameters.put("pg_size", "10");
        parameters.put("start_index", "0");
        parameters.put("mode", "cancel");
        parameters.put("auth_token", auth_token);
        Log.e("para", parameters.toString());
        pd = new Progressloadingdialog(UserCancelRequest.this, "Loading...");
        pd.setCancelable(false);
        pd.show();
        HttpAsync httpAsync = new HttpAsync(context, asyncTaskListener, Constants.BASE_URL + "user_requests/bought_requests/get.json?", parameters, 2, "confirm");
        httpAsync.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
        if (result.equalsIgnoreCase("fail")) {
            ShowSnackBar();
        } else if (tag.equalsIgnoreCase("confirm")) {
            pd.dismiss();
            Log.e("result", result);
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    JSONArray jsonArray = job.getJSONArray("canceled_bookings");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Confirm_Bean b = new Confirm_Bean();
                            b.setCid(jsonObject.getString("id"));
                            b.setOffered_room_type(jsonObject.optString("offered_room_type"));
                            b.setAddress(jsonObject.optString("address"));
                            b.setItem_name(jsonObject.getString("item_name"));
                            b.setHotel_name(jsonObject.optString("hotel_name"));
                            b.setHotel_class(jsonObject.optString("hotel_class"));

                            JSONObject jsonObject1 = jsonObject.optJSONObject("offer_detail");
                            if (jsonObject1 != null) {
                                b.setHotel_price(jsonObject1.getString("offer_price"));
                                b.setCurrency(jsonObject1.getString("currency_code"));
                            } else {
                                b.setHotel_price("Offer Details. Cannot be received.");
                                b.setCurrency("");
                            }
                            rowitems.add(b);

                            if (Long.parseLong(b.getCid()) == highlightItemId) {
                                scrollPos = i;
                            }

                        }
                        UserCancelBookingAdapter adpt = new UserCancelBookingAdapter(context, rowitems);
                        lv.setAdapter(adpt);
                        lv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lv.smoothScrollToPosition(scrollPos);
                            }
                        }, 500);
                    } else {
                        nobook.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }

        }
    }
}
