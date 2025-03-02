package com.cheqin.booking.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.cheqin.booking.R;
import com.cheqin.booking.dialogs.OderDeliveredOtp;
import com.cheqin.booking.dialogs.PostResquestConfirmationDialog;
import com.cheqin.booking.mappager.model.TopFiveLiveOffer;
import com.cheqin.booking.network.APIController;
import com.cheqin.booking.network.APIResponseListener;
import com.cheqin.booking.network.models.response.AbstractResponse;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.PayU_Gateway;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Admin on 12/14/2015.
 */
public class PreparePayOnline extends AppCompatActivity implements AsyncTaskListener {

    private AsyncTaskListener listener = null;
    private HashMap<String, String> parameter1 = null;
    private Progressloadingdialog progressDialog1 = null;
    private String securityid = "", purchaseid = "";
    private String dealid = null;
    private String quantity = null;
    private String billaddress = null;
    private String shipaddress = null;
    private String pin = null;
    private String name = null;
    private String city = null;
    private String state = null;
    private String about = "mydeals247";
    private String amount = "1";
    private String Redeemamount = "0";
    private String price = null;
    private Button btn_proceed = null;
    private HashMap<String, String> parameter = null;
    private Progressloadingdialog progressDialog = null;
    private SharedPreferences image_url = null;
    private SharedPreferences bookdetails = null;
    private SharedPreferences pricey = null;
    private String auth_token = null;
    private String id = null;

    private EditText fname = null;
    private EditText email = null;
    private TextView phnum = null;
    private TextView adults = null;
    private TextView childrens = null;
    private TextView rooms = null;
    private TextView hoteltype = null;
    private TextView Terms = null;
    private TextView Conditions = null;
    private TextView budget = null;
    private CheckBox check = null;
    SettingSharedPrefs ssp;
    private String str_price = null;
    private Button btn_pay_at_hotel = null;
    Intent intent = null;

    private TopFiveLiveOffer offer;
    //TextView agreement;
    // CheckBox agreement_confirm;
    CheckBox agreement_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shipping_address);
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //    agreement = (TextView) findViewById(R.id.agreement);
        //  agreement_confirm = (CheckBox) findViewById(R.id.agreement_confirm);
        agreement_terms = (CheckBox) findViewById(R.id.agreement_terms);

        fname = (EditText) findViewById(R.id.pay_fname);
        email = (EditText) findViewById(R.id.pay_email);
        phnum = (TextView) findViewById(R.id.pay_phnum);
        adults = (TextView) findViewById(R.id.pay_adults);
        childrens = (TextView) findViewById(R.id.pay_childrens);
        rooms = (TextView) findViewById(R.id.pay_rooms);
        hoteltype = (TextView) findViewById(R.id.pay_hoteltype);
        Terms = (TextView) findViewById(R.id.pay_terms);
        Conditions = (TextView) findViewById(R.id.pay_conditions);
        budget = (TextView) findViewById(R.id.pay_budget);
        check = (CheckBox) findViewById(R.id.pay_checkterms);
        btn_pay_at_hotel = (Button) findViewById(R.id.btn_pay_at_hotel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Booking Review");

        intent = getIntent();

        offer = new Gson().fromJson(intent.getStringExtra("offer"), TopFiveLiveOffer.class);

        Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserTermsandConditions.class));
                finish();
            }
        });

        Conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserTermsandConditions.class));
                finish();
            }
        });
        String currencyCode = offer.getCurrencyCode() == null || offer.getCurrencyCode().equals("") ? "INR" : offer.getCurrencyCode();
        bookdetails = getSharedPreferences("deatils", MODE_PRIVATE);
        rooms.setText(bookdetails.getString("rooms", null));
        adults.setText(bookdetails.getString("adults", null));
        childrens.setText(bookdetails.getString("children", null));
        hoteltype.setText(bookdetails.getString("hoteltype", null));

        budget.setText(Common.getCurrencySymbol(currencyCode) + offer.getOfferPrice());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date date = format.parse(offer.getOfferExpired());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            //    agreement.setText ("YOU WILL HAVE TO APP "+currencyCode + " " +(0.12*offer.getOfferPrice())+" TOWARDS THE COMMISSION TO Utel.app(12% ON TOTAL) ON OR BEFORE "+ formatter.format(date));
            //  agreement_confirm.setText("By checking this box you agree to pay the commission of "+currencyCode + " " +(0.12*offer.getOfferPrice())+" to Utel.app on or before "+ formatter.format(date));

        } catch (Exception e) {
            e.printStackTrace();
        }
       /*agreement_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent8 = new Intent(PreparePayOnline.this, HotelierTermsandConditions.class);
                intent8.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent8);
            }
        });*/
        str_price = bookdetails.getString("price", null);
        listener = PreparePayOnline.this;
        progressDialog = new Progressloadingdialog(PreparePayOnline.this, "Loading...");
        progressDialog1 = new Progressloadingdialog(PreparePayOnline.this, "Please wait...");

        auth_token = ssp.getPRE_auth_token();

        image_url = getSharedPreferences("image", MODE_PRIVATE);
        id = image_url.getString("id", null);

//        pricey = getSharedPreferences("price", MODE_PRIVATE);
//        str_price = pricey.getString("price", null);
//        Log.e("provds",str_price);
//        final String str_hotelname = pricey.getString("hotel_name", null);

        parameter1 = new HashMap<>();
        parameter1.put("auth_token", auth_token);
        Log.e("para_scode", parameter1.toString());
        progressDialog1.setCancelable(false);
        progressDialog1.show();
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "users/user_info/get.json?", parameter1, 2, "data");
        httpAsync.execute();

        btn_pay_at_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                sendPurchaseOtp();
//                postPurchaseOtpReq();

                //if(agreement_confirm.isChecked()) {
                if (agreement_terms.isChecked()) {
                    parameter1 = new HashMap<>();
                    parameter1.put("auth_token", auth_token);
                    parameter1.put("mofr_id", String.valueOf(offer.getId()));
                    parameter1.put("currency_code", currencyCode);
                    Log.e("para_pay_hotel", parameter1.toString());
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "user_requests/pay_at_hotel/pay.json?", parameter1, 2, "pay_hotel");
                    httpAsync.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
                }
                /*}else{
                    Toast.makeText(getApplicationContext(),"Please accept Agreement",Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        btn_proceed = (Button) findViewById(R.id.btnPay);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Agree the Terms ", Toast.LENGTH_LONG).show();
                } else {

                    parameter = new HashMap<>();
                    parameter.put("auth_token", auth_token);
                    parameter.put("mid", id);
                    parameter.put("qty", "1");
                    parameter.put("bill_address", billaddress);
                    parameter.put("ship_address", shipaddress);
                    parameter.put("postal_code", pin);
                    parameter.put("name", name);
                    parameter.put("city", city);
                    parameter.put("state", state);
                    parameter.put("redeemed_amt", "0");
                    parameter.put("online_trn_amt", str_price);
                    Log.e("para", parameter.toString());
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "user_requests/prepare_buy/post.json?", parameter, 2, "pay");
                    httpAsync.execute();
                }

            }
        });

    }

    public void sendPurchaseOtp() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        OderDeliveredOtp dialogFragment = OderDeliveredOtp.Companion.newInstance(ssp.getPRE_USER_PROFILE_mobile());

        dialogFragment.show(fragmentTransaction, "dialog");
        dialogFragment.setClicksInterface(new OderDeliveredOtp.DialogClickInterface() {
            @Override
            public void sendOtp() {
                postPurchaseOtpReq();
            }

            @Override
            public void submitOtp(@Nullable String otp) {

            }
        });

    }

    public void postPurchaseOtpReq() {

        APIController apiController = APIController.getInstance(this);
        Call<Response<AbstractResponse>> callit = apiController.apiServices.purchaseOTP(offer.getId() + "", ssp.getPRE_auth_token(), "pay");
        apiController.retrofitRequest(callit, new APIResponseListener() {
            @Override
            public void onSuccess(int reqCode, Object responseObject) {
                if (responseObject instanceof AbstractResponse) {

                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }
        });
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

        Log.e("response", result);

        if (result.equalsIgnoreCase(Constants.FAIL)) {

        } else if (tag.equalsIgnoreCase("data")) {
            progressDialog1.dismiss();
            try {
                JSONObject job = new JSONObject(result);

                if (job != null) {

                    JSONObject jo_new = job.optJSONObject("user");
                    if (jo_new != null) {
                        fname.setText(emptyifnull(jo_new.optString("fname")));
                        email.setText(emptyifnull(jo_new.optString("email")));
                        name = (emptyifnull(jo_new.optString("display_name")));
                        phnum.setText(emptyifnull(jo_new.optString("mobile")));
                    }
                    JSONObject jsonObject = job.getJSONObject("address");
                    if (jsonObject != null) {
                        city = (jsonObject.optString("city"));
                        state = (jsonObject.optString("state"));
                        billaddress = jsonObject.optString("billing_addr");
                        shipaddress = jsonObject.optString("shipping_addr");
                        pin = jsonObject.optString("zip");
                    } else {
                        Toast.makeText(getApplicationContext(), jo_new.optString("msg"), Toast.LENGTH_SHORT).show();
                    }


                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("pay")) {
            progressDialog.dismiss();
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    securityid = job.optString("reference_no");
                    purchaseid = job.optString("purchase_id");
                    Bundle bundle = new Bundle();
                    bundle.putString("price", str_price);
                    bundle.putString("about", about.toString());
                    bundle.putString("email", email.getText().toString());
                    bundle.putString("mobile", phnum.toString());
                    bundle.putString("name", name.toString());
                    bundle.putString("reference_no", securityid.toString());
                    bundle.putString("purchase_id", purchaseid.toString());

                    Intent intent = new Intent(getApplicationContext(), PayU_Gateway.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else if (tag.equalsIgnoreCase("pay_hotel")) {
            progressDialog.dismiss();

            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    if (job.optBoolean("success", true)) {
                        Toast.makeText(getApplicationContext(), "Your booking has been confirmed successfully. Now you can contact the Hotelier directly (pay at the hotel)", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), UserConfirmBooking.class);
                        intent.putExtra("offer", new Gson().toJson(offer, TopFiveLiveOffer.class));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private String emptyifnull(String text) {
        if (text == null || "".equals(text) || "null".equalsIgnoreCase(text)) {
            return "";
        } else
            return text;
    }


}
