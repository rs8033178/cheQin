package com.cheqin.booking.utils;

public class Constants {

    public static final String BASE_URL = "http://api.mypillows.company/";
    public static final String LOGIN = "my_deals/login_method.json?";
    public static final String LOGOUT = "my_deals/logout/signout.json?";
    public static final String POST = "user_requests/create/new.json?";
    public static final String REGISTRATION = "users/create_guest/new.json?";
    public static final String GET_PROFILE = "users/user_info/get.json?";
    public static final String FEEDBACK = "users/log_feedback/create.json?";
    public static final String FORGOT = "my_deals/forgot_pass/send_code.json?";
    public static final String VERIFY = "users/verify_guest_acc/verify.json?";
    public static final String GEOCODE = "https://maps.googleapis.com/maps/api/geocode/json?";
    public static final String AUTOCOMPLETE = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

    public static final String MULTIDATE_SELECTION_MODE = "multi_date_selection";
    public static final String CHECK_IN_DATE = "check_in_date";
    public static final String CHECK_OUT_DATE = "check_out_date";
    public static final String DATE_FORMAT_PATTERN = "dd-MMM-yyyy";
    //GCM
    public static final String GCM_DELETE = "my_deals/delete_gcm/delete.json?";
    public static final int MY_PERMISSIONS_RECEIVE_SMS = 101;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final int GET_SELECTED_DATA = 2;
    public static final String PREF_CREDENTIALS = "pref_credentials";
    public static final String NO_IMAGE = "http://com.mydealsapp.mydeals247.com/system/coupon_imgs/0/medium/no_image.png";
    public static String encryption_salt = "TVq2chFR";
    public static String aes_key = "25pjLP6x";

    public static final String FAIL = "fail";
    public static final String INVALID_EMAIL_PASS = "Invalid Email or Password.";


    public interface LocalBroadcastAction {
        String ACTION_LOGOUT = "action_logout";
    }

    public interface Global {
        String DEVICE_TYPE = "android";
    }

    public interface APIRequestFragments {
        String HEADER_CONTENT_TYPE = "Content-Type";
        String HEADER_APP_VERSION = "app-version";
        String HEADER_DEVICE_TYPE = "device-type";

        String RESPONSE_TYPE_JSON = "application/json";
        String RESPONSE_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";
        String RESPONSE_TYPE_MULTIPART = "multipart/form-data";

        int CONNECT_TIMEOUT_SECS = 10;
    }

    public interface APIResponseStatus {
        int STATUS_INVALID_CREDENTIALS = 401;
        int ERROR_INTERNET_CONNECTION = 40001;
    }


    public interface APIEndPoints {
        String API_LOGIN = "/login";
        String API_GET_OTP = "/users/login_signup/do.json";
        String API_VERIFY_OTP = "/users/verify_loginOTP/verify.json";
        String API_SIGN_UP = "/users/login_signup_action/doaction.json";
        String API_SEND_PURCHASE_OTP = "/user_requests/send_purchase_otp/send.json";
    }

}
