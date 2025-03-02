package com.cheqin.booking.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Adapter.UserLiveListAdapter;
import com.cheqin.booking.Bean.Userbean;
import com.cheqin.booking.R;
import com.cheqin.booking.mappager.TopOffersMapActivity;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Logger;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.cheqin.booking.gcm.NotificationNavigationHelperKt.OBJECT_ID;

public class UserLiveRequests extends AppCompatActivity implements AsyncTaskListener {

    private static final String TAG = UserLiveRequests.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 300;

    private ListView list = null;
    private HashMap<String, String> para = null;
    private ArrayList<Userbean> rowItems;
    private AsyncTaskListener listener = null;
    private UserLiveListAdapter userLiveListAdapter = null;
    private Context context = null;
    private Progressloadingdialog pd = null;
    private SharedPreferences offerid = null;
    private String auth_token = null;
    private PullToRefreshView mPullToRefreshView = null;
    private int REFRESH_DELAY = 2000;
    private LinearLayout nobook = null;
    private Button Bargain_now = null;

    SettingSharedPrefs ssp;

    DecimalFormat commaformatter;
    private long highlightItemId = -1;
    private int scrollPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_live_requests);
        //  initSMSWork();
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
        context = getApplicationContext();
        list = (ListView) findViewById(R.id.listView);
        listener = UserLiveRequests.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Bargained Offers");


        highlightItemId = getIntent().getLongExtra(OBJECT_ID, -1);

        commaformatter = new DecimalFormat("#,###,###");
        nobook = (LinearLayout) findViewById(R.id.hotel_no_request3);
        Bargain_now = (Button) findViewById(R.id.hotel_no_request4);

        Bargain_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserBooking.class);
                startActivity(intent);
            }
        });

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(() -> {
            para = new HashMap<>();
            //Log.d(TAG, "Auth Token : "+auth_token);
            para.put("auth_token", auth_token);

//                pd= new ProgressDialog(UserLiveRequests.this);
//                pd.setMessage("Loading");
//                pd.setCancelable(true);
//                pd.setIndeterminate(true);
//                pd.show();

            HttpAsync httpAsync = new HttpAsync(context, listener, Constants.BASE_URL + "user_requests/list_requests/get.json?", para, 1, null);
            httpAsync.execute();
            mPullToRefreshView.postDelayed(() -> {
                mPullToRefreshView.setRefreshing(false);
//                        mPullToRefreshView.setRefreshing(boolean isRefreshing)
            }, REFRESH_DELAY);
        });

        //  rowItems = new ArrayList<Userbean>();

        auth_token = ssp.getPRE_auth_token();

        checkConnection();

        offerid = getSharedPreferences("offer_id", Context.MODE_PRIVATE);

        list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                list.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                list.smoothScrollToPosition(scrollPos);

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!rowItems.get(position).isActionEnabled()) {
                    return;
                }

                rowItems.get(position).getAdults();
                rowItems.get(position).getHotel_type();
                rowItems.get(position).getChildrens();
                rowItems.get(position).getRooms();
                rowItems.get(position).getnumb();
                rowItems.get(position).getCheck_in_date();
                rowItems.get(position).getCheck_out_date();
                rowItems.get(position).getLocality();

                if (rowItems.get(position).getOffered().equalsIgnoreCase("No Offer(s) Yet!!")) {

                } else {
                   /* Intent intent = new Intent(getApplicationContext(), UserShowMatches.class);
//                intent.putExtra("id",rowItems.get(position).getnumb());
                    startActivity(intent);

                    SharedPreferences.Editor editor = offerid.edit();
                    editor.putString("id", rowItems.get(position).getnumb());
//                    editor.putString("currency",rowItems.get(position).getCurrency());
                    editor.commit();*/

                    Intent intent = new Intent(getApplicationContext(), TopOffersMapActivity.class);
                    intent.putExtra("offer_id", rowItems.get(position).getnumb());
                    startActivity(intent);
                }
            }
        });
    }

    private void initSMSWork() {
        if (Build.VERSION.SDK_INT >= 23) {
            //Marshmallow
            marshmellowPermission();
        }
    }

    private void marshmellowPermission() {
        // Marshmallow+
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(UserLiveRequests.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserLiveRequests.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    /*
        @Override
        public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enable SMS Permissions!", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
            }

        }*/
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

            getLiveRequests();
//            getLocation();
        } else {
            ShowSnackBar();
        }
    }

    private void getLiveRequests() {
        //Log.d(TAG, "getLiveRequests "+auth_token);
        para = new HashMap<>();
        para.put("auth_token", auth_token);

        String msg = getIntent().getStringExtra("loader_msg");

        if (TextUtils.isEmpty(msg)) {
            msg = "Fetching your Requests...";
        }

        pd = new Progressloadingdialog(UserLiveRequests.this, msg);
        pd.setCancelable(false);
        pd.show();

        HttpAsync httpAsync = new HttpAsync(context, listener, Constants.BASE_URL + "user_requests/list_requests/get.json?", para, 1, null);
        httpAsync.execute();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(getApplicationContext(), UserBooking.class);
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
        finish();
    }

    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onTaskCancelled(String data) {

    }


    @Override
    public void onTaskComplete(String result, String tag) {
        if (UserLiveRequests.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
            return;
        }
        //    pd.dismiss();
        dismissProgressDialog();
        Log.e("Result is : ", result);

        if (result.equalsIgnoreCase("fail")) {

            ShowSnackBar();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    JSONArray liveRequests = jsonObject.getJSONArray("user_requests_live");
                    JSONArray expiredRequests = jsonObject.getJSONArray("user_requests_expired");
                    Logger.debugEntire(result);
                    rowItems = null;
                    rowItems = new ArrayList<Userbean>();
                    if (liveRequests.length() > 0) {
                        for (int i = 0; i < liveRequests.length(); i++) {
                            if (jsonObject != null) {
                                Userbean b = new Userbean();
                                JSONObject jsonObject1 = liveRequests.getJSONObject(i);
                                b.setnumb(jsonObject1.optString("id"));

                                b.setExhibition(jsonObject1.optBoolean("is_wedding_exhibition"));
                                b.setCheck_in_date(jsonObject1.optString("check_in_date"));
                                b.setCheck_out_date(jsonObject1.optString("check_out_date"));

                                if (b.isExhibition()) {
                                    b.setCheck_in_date(formatDate(jsonObject1.optString("check_in_date")));
                                    b.setCheck_in_date1(formatDate(jsonObject1.optString("check_in_date1")));
                                    b.setCheck_in_date2(formatDate(jsonObject1.optString("check_in_date2")));
                                }
                                b.setExhibitionCategoryName(jsonObject1.optString("category_name"));

                                b.setRooms(jsonObject1.optString("no_of_rooms"));
                                b.setAdults(jsonObject1.optString("adult"));
                                b.setChildrens(jsonObject1.optString("children"));
                                b.setLocality(jsonObject1.optString("locality"));
                                b.setHotel_type(jsonObject1.optString("item_info"));
                                b.setRoom_type_id(jsonObject1.optString("room_type_id"));
                                String price = jsonObject1.optString("price");
                                String grandTotal = jsonObject1.optString("grand_total");
                                int pos = price.lastIndexOf(".");
                                try {
                                    b.setGrandTotal(Double.parseDouble(grandTotal));
                                    b.setPrice(commaformatter.format(Integer.valueOf(price.substring(0, pos))));
                                } catch (NumberFormatException nfe) {
                                    //do nothing
                                }
                                b.setShort_stay(jsonObject1.optString("is_short_term"));
                                b.setCurrency(jsonObject1.optString("currency_code"));

                                JSONArray jsonArray1 = jsonObject1.getJSONArray("top_live_offers");
                                if (jsonArray1.length() > 0) {
                                    if (jsonArray1.length() == 1) {
                                        b.setOffered(jsonArray1.length() + " Offer is waiting for you");
                                    } else {
                                        b.setOffered(jsonArray1.length() + " Offers are waiting for you");
                                    }
                                    b.setActionEnabled(true);
                                } else {
                                    if (b.isExhibition()) {
                                        b.setOffered("Please wait while offers are available");
                                    } else {
                                        b.setOffered("Please wait while Hoteliers offer the best price");
                                    }
                                    b.setActionEnabled(false);
                                }
                                rowItems.add(b);

                                if (Long.parseLong(b.getnumb()) == highlightItemId) {
                                    scrollPos = i;
                                }
                            }

                        }
                    }

                    if (expiredRequests.length() > 0) {
                        for (int i = 0; i < expiredRequests.length(); i++) {
                            if (jsonObject != null) {
                                Userbean b = new Userbean();
                                b.setExpired(true);
                                JSONObject jsonObject1 = expiredRequests.getJSONObject(i);
                                b.setnumb(jsonObject1.optString("id"));
                                b.setCheck_in_date(jsonObject1.optString("check_in_date"));
                                b.setCheck_out_date(jsonObject1.optString("check_out_date"));
                                b.setRooms(jsonObject1.optString("no_of_rooms"));
                                b.setAdults(jsonObject1.optString("adult"));
                                b.setChildrens(jsonObject1.optString("children"));
                                b.setLocality(jsonObject1.optString("locality"));
                                b.setHotel_type(jsonObject1.optString("item_info"));
                                b.setRoom_type_id(jsonObject1.optString("room_type_id"));
                                String price = jsonObject1.optString("price");
                                String grandTotal = jsonObject1.optString("grand_total");
                                int pos = price.lastIndexOf(".");
                                try {
                                    b.setGrandTotal(Double.parseDouble(grandTotal));
                                    b.setPrice(commaformatter.format(Integer.valueOf(price.substring(0, pos))));
                                } catch (NumberFormatException nfe) {
                                    //do nothing
                                }
                                b.setShort_stay(jsonObject1.optString("is_short_term"));
                                b.setCurrency(jsonObject1.optString("currency_code"));

                                b.setActionEnabled(false);
                                b.setOffered("Offer has expired");
                                rowItems.add(b);

                                if (Long.parseLong(b.getnumb()) == highlightItemId) {
                                    scrollPos = i;
                                }
                            }

                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (rowItems != null && rowItems.size() > 0) {
                userLiveListAdapter = new UserLiveListAdapter(context, rowItems);
                list.setAdapter(userLiveListAdapter);
                list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.smoothScrollToPosition(scrollPos);
                    }
                }, 500);
            } else {
                nobook.setVisibility(View.VISIBLE);
                mPullToRefreshView.setVisibility(View.GONE);
            }

        }
    }

    private String formatDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        try {
            Date date = format.parse(dateString);
            SimpleDateFormat toFormat = new SimpleDateFormat("dd MMM,yyyy");
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


}
