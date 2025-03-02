package com.cheqin.booking.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Adapter.UserConfirmBookingAdapter;
import com.cheqin.booking.Bean.Confirm_Bean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cheqin.booking.gcm.NotificationNavigationHelperKt.OBJECT_ID;

public class UserConfirmBooking extends AppCompatActivity implements AsyncTaskListener {

    private AsyncTaskListener asyncTaskListener = null;
    private HashMap<String, String> parameters = null;
    private HashMap<String, String> paraHashMap = null;
    private HashMap<String, String> para = null;
    private Context context = null;
    private ListView lv = null;
    private Progressloadingdialog pd = null;
    private String auth_token = null;
    private List<Confirm_Bean> rowitems = null;
    private TextView nobook = null;
    private String cid = null;

    SettingSharedPrefs ssp;
    private UserConfirmBookingAdapter adpt;

    private long highlightItemId = -1;
    private int scrollPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_confirm_booking);

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Confirmed Bookings");

        lv = (ListView) findViewById(R.id.user_confirm_list);
        nobook = (TextView) findViewById(R.id.user_no_booking);
        context = getApplicationContext();
        asyncTaskListener = UserConfirmBooking.this;

        highlightItemId = getIntent().getLongExtra(OBJECT_ID, -1);

        auth_token = ssp.getPRE_auth_token();

        checkConnection("booked", 0);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserConfirmBooking.this);
            alertDialogBuilder.setTitle("Cancel")
                    .setMessage("Are you sure you want to Cancel the Booking?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            checkConnection("cancel", position);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialogBuilder.show();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(getApplicationContext(), UserBooking.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();

    }


    private void ShowSnackBar(final String tag, final int position) {

        String styledText = "<font color='black'>No Internet Connection</font>.";
        // msnackBar;
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", v -> checkConnection(tag, position))
                .setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();


        snackBarView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    public void checkConnection(String tag, int position) {

        if (Common.isNetworkAvailable(getApplicationContext())) {

            if (tag.equalsIgnoreCase("booked")) {
                getBookedRequests();
            } else if (tag.equalsIgnoreCase("cancel")) {
                cancelbooking(position);
            }
//            getLocation();
        } else {
            ShowSnackBar(tag, position);
        }
    }

    private void cancelbooking(final int position) {


        cid = rowitems.get(position).getCid();
        paraHashMap = new HashMap<>();
        paraHashMap.put("boffer_id", rowitems.get(position).getCid());
        paraHashMap.put("auth_token", auth_token);


        para = new HashMap<String, String>();
        para.put("auth_token", auth_token);
        para.put("ofid", cid);
        para.put("refund_note", "");
        para.put("status", "");
        pd = new Progressloadingdialog(UserConfirmBooking.this, "Cancelling Booking....");
        pd.setCancelable(false);
        pd.show();
        HttpAsync httpAsync1 = new HttpAsync(context, asyncTaskListener, Constants.BASE_URL + "user_requests/cancel_booking/cancel.json?", para, 2, "cancel");
        httpAsync1.execute();
        if (adpt != null && adpt.getCount() > position) {
            rowitems.remove(position);
            adpt.notifyDataSetChanged();
        }




       /* final HttpAsync pass = new HttpAsync(getApplicationContext(), new AsyncTaskListener() {
            @Override
            public void onTaskCancelled(String data) {

            }

            @Override
            public void onTaskComplete(String result, String tag) {

                if (result.equalsIgnoreCase("fail")) {
                    ShowSnackBar(tag, position);
                } else {
                    try {
                        JSONObject job = new JSONObject(result);
                        if (job != null) {
                            if (job.optBoolean("success", true)) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserConfirmBooking.this);
                                alertDialogBuilder.setMessage(Html.fromHtml(job.optString("preview_msg")))
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(context, UserConfirmBooking.class));
                                                finish();

                                                para = new HashMap<String, String>();
                                                para.put("auth_token", auth_token);
                                                para.put("ofid", cid);
                                                para.put("refund_note", "");
                                                para.put("status", "");
                                                pd = new Progressloadingdialog(UserConfirmBooking.this, "Cancelling Booking....");
                                                pd.setCancelable(false);
                                                pd.show();
                                                HttpAsync httpAsync1 = new HttpAsync(context, asyncTaskListener, Constants.BASE_URL + "user_requests/cancel_booking/cancel.json?", para, 2, "cancel");
                                                httpAsync1.execute();
//                                        pd = new Progressloadingdialog(UserConfirmBooking.this,"Loading");
//                                        pd.setCancelable(false);
//                                        pd.show();
//                                        HttpAsync httpAsync = new HttpAsync(context, asyncTaskListener,Constants.BASE_URL + "user_requests/bought_requests/get.json?", parameters, 2, "confirm");
//                                        httpAsync.execute();

                                            }
                                        });
                                alertDialogBuilder.show();
                            } else {
                                Toast.makeText(getApplicationContext(), job.optString("preview_msg"), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, Constants.BASE_URL + "user_requests/cancel_preview/preview.json?", paraHashMap, 2, null);
        pass.execute();*/

    }

    private void getBookedRequests() {

        parameters = new HashMap<>();
        parameters.put("pg_size", "10");
        parameters.put("start_index", "0");
        parameters.put("mode", "confirm");
        parameters.put("auth_token", auth_token);
        Log.e("para", parameters.toString());
        pd = new Progressloadingdialog(UserConfirmBooking.this, "Loading...");
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
        if (pd != null) {
            pd.dismiss();
        }
        if (result.equalsIgnoreCase("fail")) {
            ShowSnackBar(tag, 0);
        } else if (tag.equalsIgnoreCase("confirm")) {
            Log.e("result", result);
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    JSONArray jsonArray = job.getJSONArray("confirmed_bookings");
                    rowitems = null;
                    rowitems = new ArrayList<>();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Confirm_Bean b = new Confirm_Bean();

                            b.setHotel_name(jsonObject.optString("hotel_name"));
                            b.setHotel_class(jsonObject.optString("hotel_class"));
                            b.setOffered_room_type(jsonObject.optString("offered_room_type"));
                            b.setAddress(jsonObject.optString("address"));
                            b.setCid(jsonObject.getString("id"));


                            JSONObject jsonObject1 = jsonObject.optJSONObject("offer_detail");
                            if (jsonObject1 != null) {
                                b.setHotel_price(jsonObject1.getString("offer_price"));
                                b.setHotel_latitude(jsonObject1.optDouble("hotel_latitude"));
                                b.setHotel_longitude(jsonObject1.optDouble("hotel_longitude"));
                            } else {
                                b.setHotel_price("");
                            }

                            b.setHotelier_email(jsonObject.getString("hotelier_email"));
                            b.setHotelier_mobile(jsonObject.getString("hotelier_mobile"));
                            b.setCurrent_status(jsonObject.getString("current_status"));

                            JSONObject requestDetail = jsonObject.getJSONObject("request_detail");
                            b.setRequestLatitude(requestDetail.optDouble("latitude"));
                            b.setRequestLongitude(requestDetail.optDouble("longitude"));
                            b.setCheck_in_date(requestDetail.getString("check_in_date").substring(0, 10));
                            b.setCheck_out_date(requestDetail.getString("check_out_date").substring(0, 10));
                            b.setCurrency(requestDetail.getString("currency_code"));

                            rowitems.add(b);

                            if (Long.parseLong(b.getCid()) == highlightItemId) {
                                scrollPos = i;
                            }

                        }
                        adpt = new UserConfirmBookingAdapter(UserConfirmBooking.this, rowitems, UserConfirmBooking.this);
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
        } else if (tag.equalsIgnoreCase("cancel")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    if (job.optBoolean("success", true)) {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }

        }
    }
}
