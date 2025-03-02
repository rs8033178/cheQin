package com.cheqin.booking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Log.GenerateAuthtoken;
import com.cheqin.booking.Log.HotelierRegistration;
import com.cheqin.booking.Log.PreForgotPassword;
import com.cheqin.booking.Log.SocialRegistration;
import com.cheqin.booking.Log.UserRegistration;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.User.UserLiveRequests;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class LoginActivity extends AppCompatActivity implements AsyncTaskListener, View.OnClickListener{
    private String PRE_rememberme = "remember_me";
    public Button login = null;
    public TextView register = null;
    public EditText edt_email = null;
    public EditText edt_password = null;
    private Progressloadingdialog pDialog;
    private HashMap<String, String> para = null;
    private HashMap<String, String> postParameter = null;
    private AsyncTaskListener listener = null;
    private TextView forgot_pass = null;
    private HashMap<String, String> prof = null;
    CheckBox rememberMeCB;
    String Authtoken = null;

    private boolean isPostOffer = false;

    private Bundle bundle = null;
    private Bundle bundleuser = null;
    private String user_type = null;
    private String usertype = null;
    private ImageView visible_butt = null;
    private ImageView invisible_butt = null;
    private ImageView check_icon = null;
    TextWatcher textWatcher;

    private SharedPreferences shd_hotelname = null;
    String str_hotelname = null;

    private String email = null;
    private String personName = null;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    AccessToken accessToken;

    SettingSharedPrefs ssp;
    LoginButton loginButton;
    SharedPreferences remeberMePrefs;
    String usernamePref="usernamePref",passwordPref="passwordPref",cbStatusPref="cbStatusPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initlization();
        setContentView(R.layout.login);
        listener = LoginActivity.this;
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
        remeberMePrefs = getSharedPreferences("REMEMBERME", Context.MODE_PRIVATE);
        initButton();
        edt_email = (EditText) findViewById(R.id.email);
        edt_password = (EditText) findViewById(R.id.editText2);
        check_icon = (ImageView) findViewById(R.id.check_icon);
        register = (TextView) findViewById(R.id.tv_signup);
        login = (Button) findViewById(R.id.login);
        forgot_pass = (TextView) findViewById(R.id.forgot);
        visible_butt = (ImageView) findViewById(R.id.visible_butt);
        invisible_butt = (ImageView) findViewById(R.id.invisible_butt);
        rememberMeCB = (CheckBox) findViewById(R.id.rememberme);

        if((remeberMePrefs.getBoolean(cbStatusPref,false))){
            edt_email.setText(remeberMePrefs.getString(usernamePref,""));
            edt_password.setText(remeberMePrefs.getString(passwordPref,""));
            rememberMeCB.setChecked(remeberMePrefs.getBoolean(cbStatusPref,false));
        }
        edt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {


                if (hasFocus) {
                    // If view having focus.

                    edt_email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                            Matcher matcher = pattern.matcher(edt_email.getText().toString());
                            if (matcher.matches()) {
                                check_icon.setVisibility(View.VISIBLE);
                            } else {
                                check_icon.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    // If view not having focus. You can validate here
                    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                    Matcher matcher = pattern.matcher(edt_email.getText().toString());
                    if (!matcher.matches() || edt_email.getText().toString().equalsIgnoreCase("")) {
                        edt_email.setError("enter a valid email address");
                    }
                }
            }
        });
        shd_hotelname = getSharedPreferences("hotel_name", MODE_PRIVATE);
        str_hotelname = shd_hotelname.getString("hotelname", null);

        postParameter = UserBooking.para1;
        bundle = getIntent().getExtras();


        if (bundle != null) {
            isPostOffer = bundle.getBoolean("post");
        }

        bundleuser = getIntent().getExtras();

        if (bundleuser != null) {
            user_type = bundleuser.getString("type");
        }
//        Log.e("user_type",bundleuser.getString("type"));

        if (user_type != null) {
            if (user_type.equalsIgnoreCase("buysell")) {
                loginButton.setVisibility(View.GONE);
            }
        }

     //
        //  Log.e("bundle", isPostOffer + " ");

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent prof = new Intent(LoginActivity.this, PreForgotPassword.class);
                prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof);
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edt_email.getText().toString().equalsIgnoreCase("")) || (edt_password.getText().toString().equalsIgnoreCase(""))) {
                    login.setBackgroundResource(R.drawable.btn_login_bg);
                    login.setClickable(false);
                } else {
                    //  signupButton.setEnabled(true);
                    login.setBackgroundResource(R.drawable.btn_login_bg);
                    login.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((edt_email.getText().toString().equalsIgnoreCase("")) || (edt_password.getText().toString().equalsIgnoreCase(""))) {
                    login.setBackgroundResource(R.drawable.btn_login_bg);
                    login.setClickable(false);
                    visible_butt.setVisibility(View.GONE);
                    invisible_butt.setVisibility(View.GONE);
                } else {
                    //  signupButton.setEnabled(true);
                    login.setBackgroundResource(R.drawable.btn_login_bg);
                    login.setClickable(true);
                    visible_butt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        visible_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                visible_butt.setVisibility(View.GONE);
                invisible_butt.setVisibility(View.VISIBLE);
            }
        });

        invisible_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                invisible_butt.setVisibility(View.GONE);
                visible_butt.setVisibility(View.VISIBLE);
            }
        });

        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    onLogin();
                }
                return true;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_email.getText().toString().equalsIgnoreCase("") || (!edt_password.getText().toString().equalsIgnoreCase(""))) {
                    onLogin();
                } else {
                    login.setClickable(false);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_type != null) {
                    if (isPostOffer || user_type.equalsIgnoreCase("buyer")) {
                        Intent register = new Intent(getApplicationContext(), UserRegistration.class);
                        register.putExtra("post", bundle.getBoolean("post"));
                        register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        register.putExtra("type", user_type);
                        startActivity(register);
                    } else {
                        Intent Hotelreg = new Intent(getApplicationContext(), HotelierRegistration.class);
                        Hotelreg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(Hotelreg, 200);
                    }
                } else {
                    Intent register = new Intent(getApplicationContext(), UserRegistration.class);
                    register.putExtra("post", bundle.getBoolean("post"));
                    register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(register);
                }
            }
        });
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            //CAAHbcPcQKT4BAOsTN44cLnyh5BycncKBCcvGaCP06WpzM585h1S9d6ZA3AwHZBDqKNuQbNTdgW6o0WfBIr7lKrdavZBugztwX9KiQGkLnPZAC31wRRsjUSyLG2XIjb1JfkmCZCX2mHZCAKbycDO1SEGzo6ZCVHXqfHtujTPnSOOevc7g08OZAH6wvT8Y5JIetGjmvzQIoxfLZBXPzXypFgdXX
          //  Log.e("auth_token", accessToken.getToken());
            Profile profile = Profile.getCurrentProfile();
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                personName = object.getString("name");
                                email = object.getString("email");
                             //   Log.e("email", email);
                               // Log.e("name", personName);
                                getDetails();
//                                String birthday = object.getString("birthday");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // 01/31/1980 format
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

//            personName = profile.getFirstName();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }

    };

    private void getDetails() {

        String gAuthtoken = GenerateAuthtoken.generate(email, accessToken.getToken(), "gmail");

       // Log.e("auth token is ", gAuthtoken);
        pDialog = new Progressloadingdialog(LoginActivity.this, "Logging in");
        pDialog.show();

        para = new HashMap<>();
        para.put("auth_token", gAuthtoken);
        HttpAsync tokenAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.LOGIN, para, 2, "google_login");
        tokenAsync.execute();

    }


    private void initlization() {
        FacebookSdk.sdkInitialize(LoginActivity.this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void initButton() {
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        // loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

//    private void displayMessage(Profile profile){
//        if(profile != null){
//            textView.setText(profile.getName());
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
//        Log.e("profile",profile.toString());
//        displayMessage(profile);
    }


//    facebook
//    private FacebookCallback<LoginResult> mcallback= new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            AccessToken accessToken=loginResult.getAccessToken();
//            Profile profile=Profile.getCurrentProfile();
//
//            if(profile!=null)
//            {
//                tv1.setText(profile.getName());
//                fbpic.setImageURI(profile.getProfilePictureUri(30,30));
//
//                Toast.makeText(getApplicationContext(), profile.getName(), Toast.LENGTH_LONG).show();
//            }
//          Intent f=new Intent(getApplicationContext(),fbdetails.class);
//           startActivity(f);
//
//        }
//        @Override
//        public void onCancel() {
//
//        }
//
//        @Override
//        public void onError(FacebookException error) {
//
//        }    };

    public void onLogin() {
        SharedPreferences.Editor editor = remeberMePrefs.edit();
        if(rememberMeCB.isChecked()){
            editor.putString(usernamePref,edt_email.getText().toString());
            editor.putString(passwordPref,edt_password.getText().toString());
            editor.putBoolean(cbStatusPref,rememberMeCB.isChecked());
            editor.commit();
        }else{
            editor.clear().commit();
        }
        hideSoftKeyboard();
        checkConnectionAndRegistration();
    }

    private void checkConnectionAndRegistration() {
        if (Common.isNetworkAvailable(getApplicationContext())) {
            doLogin();
        } else {
            //Toast.makeText(getApplicationContext(),"Internet is not Avilable",Toast.LENGTH_SHORT).show();
            ShowSnackBar();
        }
    }

    private void doLogin() {

        Authtoken = GenerateAuthtoken.generate(edt_email.getText().toString(), edt_password.getText().toString(), "normal");

     //   Log.e("auth token is ", Authtoken);
        pDialog = new Progressloadingdialog(LoginActivity.this, "Logging in..");
        pDialog.setCancelable(false);
        pDialog.show();
        para = new HashMap<>();
        para.put("auth_token", Authtoken);
        HttpAsync tokenAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.LOGIN, para, 2, "login");
        tokenAsync.execute();

    }

    private void ShowSnackBar() {
        String styledText = "<font color='white'>No Internet Connection</font>.";
        // msnackBar;
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnectionAndRegistration();
                    }
                })
                .setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#000000")); // snackbar background color
        // snackbar.setActionTextColor(Color.parseColor("#000000")); // snackbar action text color
        snackbar.show();
    }


//    public void signup(View v) {
//        if (isPostOffer || user_type.equalsIgnoreCase("buyer")) {
//            Intent register = new Intent(getApplicationContext(), UserRegistration.class);
//            startActivity(register);
//        } else {
//            Intent Hotelreg = new Intent(getApplicationContext(), HotelierRegistration.class);
//            startActivityForResult(Hotelreg, 200);
//        }
//    }


    @Override
    public void onTaskCancelled(String data) {
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskComplete(String result, String tag) {

        if (result.equalsIgnoreCase("fail")) {

            ShowSnackBar();

//            Toast.makeText(getApplicationContext(), "please check your internet connection and try again!!!", Toast.LENGTH_LONG).show();

        } else if (tag.equalsIgnoreCase("login")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                  //  Log.e("success  nj", job.getBoolean("success") + " ");
                    //Log.e("msg", job.getString("msg"));
                    if (job.getBoolean("success")) {
                        pushRegister(edt_email.getText().toString(),job.getString("user_id"));
                        usertype = job.getString("user_type");

                        if (usertype.equalsIgnoreCase("buyer")) {

                            ssp.setPRE_user_login(true);
                            ssp.setPRE_hotelier_login(false);
                            ssp.setPRE_auth_token(Authtoken);
                            ssp.setPRE_usertype(usertype);

                            if (isPostOffer) {
                                String URL1 = " http://api.mypillows.company/user_requests/create/new.json?";
                                UserBooking.para1.put("auth_token", Authtoken);
                                HttpAsync user_values = new HttpAsync(getApplicationContext(), listener, URL1, postParameter, 2, "user_values");
                                user_values.execute();
//                                startActivity(new Intent(getApplicationContext(), UserLiveRequests.class));
                                Intent prof = new Intent(LoginActivity.this, UserLiveRequests.class);
                                prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(prof);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), UserLiveRequests.class));
                                Intent prof = new Intent(LoginActivity.this, UserLiveRequests.class);
                                prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(prof);
                                finish();
                            }
                        } else if (job.optString("user_type").equalsIgnoreCase("buysell")) {
                                Toast.makeText(getApplicationContext(),"Please SignUp",Toast.LENGTH_SHORT).show();
                          /* ssp.setPRE_user_login(false);
                            ssp.setPRE_hotelier_login(true);
                            ssp.setPRE_auth_token(Authtoken);
                            ssp.setPRE_usertype(usertype);

//                            Toast.makeText(getApplicationContext(), "you are logged in as" + edt_email.getText().toString(), Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(getApplicationContext(), HotelierShowRequests.class));
                            Intent prof = new Intent(LoginActivity.this, HotelierShowRequests.class);
                            prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(prof);
                            finish();*/

                        }
                    } else if (!job.getBoolean("success")) {
                       // Log.e("not going", "false");
                        if (job.getString("msg").equalsIgnoreCase("Email ID is not yet verified.")) {
                            ssp.setPRE_user_login(false);
                            ssp.setPRE_hotelier_login(false);
                            ssp.setPRE_auth_token(Authtoken);
                            Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Verify.class);
                            intent.putExtra("emailid", edt_email.getText().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), SocialRegistration.class);
//                        intent.putExtra("email", email);
//                        intent.putExtra("name", personName);
//                        startActivity(intent);
//                        finish();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Please SignUp", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("google_login")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    //Log.e("success", job.getBoolean("success") + " ");
                    //Log.e("msg", job.getString("msg"));

                    if (job.getBoolean("success")) {
//                    if (job.optBoolean("success", true)) {

                        String temp_pass = job.getString("temp_password");
                      //  Log.e("temppass", temp_pass);
                        String temp_pass1 = GenerateAuthtoken.decryptpassword(temp_pass);
                        //Log.e("decry_temppass", temp_pass1);

                        Authtoken = GenerateAuthtoken.generate(email, temp_pass1, "social");

                        //Log.e("auth", Authtoken);


                        prof = new HashMap<>();
                        prof.put("auth_token", Authtoken);
                        new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + Constants.GET_PROFILE, prof, 2, "profile").execute();

                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();

                        ssp.setPRE_user_login(true);
                        ssp.setPRE_hotelier_login(false);
                        ssp.setPRE_auth_token(Authtoken);
                        ssp.setPRE_usertype(usertype);
                    } else {
//                            Log.e("dv", "ddv");
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SocialRegistration.class);
                        intent.putExtra("email", email);
                       // Log.e("name", email);
                        intent.putExtra("name", personName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

//                    } else {
//                        Log.e("msg",job.getString("msg"));
//                        ssp.setPRE_user_login(true);
//                        ssp.setPRE_hotelier_login(false);
//                        ssp.setPRE_auth_token(Authtoken);
//                        ssp.setPRE_usertype(usertype);
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(tag.equals("push_login")){
           //Log.v("vinod","push result"+result);
    }else if (tag.equalsIgnoreCase("profile")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    if (jsonObject.optBoolean("success", true)) {

                        Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), UserBooking.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    } else {

                        Intent intent = new Intent(getApplicationContext(), SocialRegistration.class);
                        intent.putExtra("email", email);
                        intent.putExtra("name", personName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
//                    JSONObject jo_new = jsonObject.getJSONObject("user");
//                    if(jo_new.getString("display_name").equalsIgnoreCase(null))
//                    {

//                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pDialog.dismiss();
    }


    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (user_type != null) {
            if (user_type.equalsIgnoreCase("buysell")) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Intent prof = new Intent(LoginActivity.this, MainActivity.class);
                prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof);
                finish();
            }
        }
    }

    // [END signIn]

    // [START signOut]
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // [START_EXCLUDE]
//                        updateUI(false);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
    // [END signOut]

    // [START revokeAccess]
//    private void revokeAccess() {
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // [START_EXCLUDE]
//                        updateUI(false);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
    // [END revokeAccess]

//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage(getString(R.string.loading));
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
//
//    private void updateUI(boolean signedIn) {
//        if (signedIn) {
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
        }
    }

    public void pushRegister( String email,String appuserid){
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isEnabled = status.getPermissionStatus().getEnabled();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        boolean subscriptionSetting = status.getSubscriptionStatus().getUserSubscriptionSetting();

        String userID = status.getSubscriptionStatus().getUserId();
        String pushToken = status.getSubscriptionStatus().getPushToken();
        HashMap para = new HashMap<>();
        para.put("uid", appuserid);
        para.put("gcm_regid", userID);
        para.put("device_type","ANDROID");

        para.put("email",email);
        HttpAsync tokenAsync = new HttpAsync(getApplicationContext(), listener, "http://api.mypillows.company/my_deals/create_updategcm/do.json", para, 2, "push_login");
        tokenAsync.execute();

        // String post= POSTCALL("http://api.mypillows.company/my_deals/create_updategcm/do.json", para);
      //  Log.v("vinod","vinod--"+post);
    }

}