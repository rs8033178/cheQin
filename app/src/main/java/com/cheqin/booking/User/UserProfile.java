package com.cheqin.booking.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Log.ChangePassword;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserProfile extends AppCompatActivity implements AsyncTaskListener {


    private Button btnEdit = null;
    private Button btnSubmit = null;
    private Button btnCancel = null;
    private EditText edtfirstname = null;
    private EditText edtlastname = null;
    private EditText edtusername = null;
    private EditText edtcity = null;
    private EditText edtmobile = null;

    private AsyncTaskListener listener = null;
    private HashMap<String, String> parameter = null;
    private Progressloadingdialog progressDialog = null;
    //    private SharedPreferences mpref = null;
    private CoordinatorLayout coordinatorLayout;
    private String auth_token = null;
    private Button change_password = null;
    private String password = null;

    SettingSharedPrefs ssp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.user_profile);
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
//                .coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Profile");

        listener = UserProfile.this;

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtfirstname = (EditText) findViewById(R.id.edtFirstname);
        edtlastname = (EditText) findViewById(R.id.edtlastname);
        edtusername = (EditText) findViewById(R.id.edtEmailID);
        edtmobile = (EditText) findViewById(R.id.edtMobile);
        edtcity = (EditText) findViewById(R.id.edt_city);
        change_password = (Button) findViewById(R.id.change_pass);

        edtfirstname.setEnabled(false);
        edtlastname.setEnabled(false);
        edtusername.setEnabled(false);
        edtmobile.setEnabled(false);
        edtcity.setEnabled(false);
//        img_gender= (ImageView) findViewById(R.id.img_gender);
//        img_emp= (ImageView) findViewById(R.id.img_emp);

//        edtemp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AlertDialog.Builder builder=new AlertDialog.Builder(UserProfile.this);
//                builder.setTitle("Select Current Status");
//                builder.setSingleChoiceItems(empstatus, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        edtemp.setText(empstatus[which]);
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.show();
//
//
//            }
//        });

//        mpref = getSharedPreferences("user_type", MODE_PRIVATE);
//        if(mpref!=null)
//        {
        auth_token = ssp.getPRE_auth_token();

        checkConnection();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnEdit.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);

                edtfirstname.setEnabled(true);
                edtlastname.setEnabled(true);
//                edtusername.setEnabled(true);
                edtmobile.setEnabled(true);
//                edtcity.setEnabled(true);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtfirstname.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(edtusername.getWindowToken(), 0);

                CharSequence checkmail = edtusername.getText().toString();

                if (edtusername.getText().toString().equalsIgnoreCase("") || edtusername.getText().toString().equalsIgnoreCase(" ")) {

                    btnEdit.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);

                    edtfirstname.setEnabled(false);
                    edtlastname.setEnabled(false);
                    edtusername.setEnabled(false);
                    edtmobile.setEnabled(false);
                    edtcity.setEnabled(false);

                    parameter.put("auth_token", auth_token);
                    parameter.put("user[fname]", edtfirstname.getText().toString());
                    parameter.put("user[lname]", edtlastname.getText().toString());
                    parameter.put("address[city]", edtcity.getText().toString());
                    parameter.put("username", edtusername.getText().toString());
                    parameter.put("address[mobile]", edtmobile.getText().toString());
                    parameter.put("address[zip]", "");
                    parameter.put("address[fax]", "");
                    parameter.put("address[billing_addr]", "");
                    parameter.put("address[shipping_addr]", "");
                    Log.e("para_profupdate", parameter.toString());
                    progressDialog = new Progressloadingdialog(UserProfile.this, "Updating Profile...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "users/update/update.json?", parameter, 2, "update");
                    httpAsync.execute();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(checkmail).matches()) {

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Enter a valid Username ", Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                    sbView.setBackgroundColor(getResources().getColor(R.color.gray_snackbar));
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();

                } else {

                    edtfirstname.setEnabled(false);
                    edtlastname.setEnabled(false);
                    edtusername.setEnabled(false);
                    edtmobile.setEnabled(false);
                    edtcity.setEnabled(false);


                    parameter.put("auth_token", auth_token);
                    parameter.put("user[fname]", edtfirstname.getText().toString());
                    parameter.put("user[lname]", edtlastname.getText().toString());
                    parameter.put("address[city]", edtcity.getText().toString());
                    parameter.put("username", edtusername.getText().toString());
                    parameter.put("address[mobile]", edtmobile.getText().toString());
                    parameter.put("address[zip]", "");
                    parameter.put("address[fax]", "");
                    parameter.put("address[billing_addr]", "");
                    parameter.put("address[shipping_addr]", "");
                    Log.e("para_profupdate", parameter.toString());
                    progressDialog = new Progressloadingdialog(UserProfile.this, "Updating Profile...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "users/update/update.json?", parameter, 2, "update");
                    httpAsync.execute();
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_change = new Intent(getApplicationContext(), ChangePassword.class);
                intent_change.putExtra("emailid", edtusername.getText().toString());
                intent_change.putExtra("oldpass", password);
                startActivity(intent_change);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
//        super.onBackPressed();
//    }


    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {

            getProfile();
//            getLocation();
        } else {
            ShowSnackBar();
        }
    }

    private void getProfile() {

        parameter = new HashMap<>();
        parameter.put("auth_token", auth_token);
        Log.e("para_scode", parameter.toString());
        progressDialog = new Progressloadingdialog(UserProfile.this, "Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "users/user_info/get.json?", parameter, 2, "data");
        httpAsync.execute();

    }


    private void ShowSnackBar() {
        String styledText = "<font color='black'>No Internet Connection</font>.";
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


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, final String tag) {

        progressDialog.dismiss();

        if (result.equalsIgnoreCase("fail")) {
            ShowSnackBar();
        } else if (tag.equalsIgnoreCase("data")) {

            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    JSONObject jo_new = job.optJSONObject("user");
                    if (jo_new != null) {
                        String firstName = jo_new.optString("fname");
                        if (!TextUtils.isEmpty(firstName) && !"null".equalsIgnoreCase(firstName)) {
                            edtfirstname.setText(firstName);
                        }

                        String lastName = jo_new.optString("lname");
                        if (!TextUtils.isEmpty(lastName) && !"null".equalsIgnoreCase(lastName)) {
                            edtlastname.setText(lastName);
                        }

                        password = jo_new.optString("system_pwd");
                        edtmobile.setText(jo_new.optString("mobile"));
                        if (!jo_new.optString("username").equalsIgnoreCase("null")) {
                            edtusername.setText(jo_new.optString("username"));
                        }
                    }
                    JSONObject jsonObject = job.optJSONObject("address");
                    if (jsonObject != null) {
                        edtcity.setText(jsonObject.optString("city"));
                    } else if (jo_new != null) {
                        String msg = jo_new.optString("msg");
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }
        } else if (tag.equalsIgnoreCase("update")) {

            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    boolean status = job.getBoolean("success");
                    if (status) {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        btnSubmit.setVisibility(View.GONE);
                        btnEdit.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getApplicationContext(), UserBooking.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, job.optString("msg"), Snackbar.LENGTH_LONG);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                        sbView.setBackgroundColor(getResources().getColor(R.color.gray_snackbar));
                        textView.setTextColor(Color.WHITE);

                        snackbar.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }

        }


    }
}
