package com.cheqin.booking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Administrator
 * <p/>
 * Class for methods which save settings values
 */
public class SettingSharedPrefs {


    public void clearSharedPreference() {
        if (pref != null) {
            pref.edit().clear().commit();
        }
    }

    private String PRE_DrawerLearned = "drawer_learned";
    //login_old,auth_token,password,usertype
    private String PRE_user_login = "user_login";
    private String PRE_app_first_lanuch = "first_launch";
    private String PRE_auth_token = "auth_token";
    private String PRE_OTP = "otp";
    private String PRE_usertype = "usertype";

    private String PRE_hotelier_login = "hotelier_login";
    private String PRE_GCM_MSG_d_status = "";

    SharedPreferences pref;
    public static SettingSharedPrefs store;

    public static SettingSharedPrefs getInstance(Context context) {
        if (store == null)
            store = new SettingSharedPrefs(context.getApplicationContext());
        return store;
    }

    private SettingSharedPrefs(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    //...................Setter method...............//
    public void setPRE_user_login(Boolean value) {
        pref.edit().putBoolean(PRE_user_login, value).commit();
    }

    public void setFirstLaunch(Boolean value) {
        pref.edit().putBoolean(PRE_app_first_lanuch, value).commit();
    }

    public void setPRE_OTP(String value) {
        pref.edit().putString(PRE_OTP, value).commit();
    }

    public void setPRE_usertype(String value) {
        pref.edit().putString(PRE_usertype, value).commit();
    }

    public void setPRE_auth_token(String value) {
//        pref.edit().putString(PRE_auth_token, value).commit();
    }
    public void setPRE_auth_tokenOtp(String value) {
        pref.edit().putString(PRE_auth_token, value).commit();
    }

    public void setPreDrawerLearned(Boolean value) {
        pref.edit().putBoolean(PRE_DrawerLearned, value).commit();
    }

    public void setPRE_hotelier_login(Boolean value) {
        pref.edit().putBoolean(PRE_hotelier_login, value).commit();
    }

    public void setPRE_GCM_MSG_d_status(Boolean value) {
        pref.edit().putBoolean(PRE_GCM_MSG_d_status, value).commit();
    }

    //...................Getter method...............//
    public boolean getPRE_user_login() {
        return pref.getBoolean(PRE_user_login, false);
    }

    public boolean getFirstLaunch() {
        return pref.getBoolean(PRE_app_first_lanuch, false);
    }

    public String getPRE_password() {
        return pref.getString(PRE_OTP, "");
    }

    public String getPRE_usertype() {
        return pref.getString(PRE_usertype, "");
    }

    public String getPRE_auth_token() {
        return pref.getString(PRE_auth_token, "");
    }

    public boolean getPreDrawerLearned() {
        return pref.getBoolean(PRE_DrawerLearned, false);
    }

    public boolean getPRE_hotelier_login() {
        return pref.getBoolean(PRE_hotelier_login, false);
    }

    public boolean getPRE_GCM_MSG_d_status() {
        return pref.getBoolean(PRE_GCM_MSG_d_status, false);
    }


    private String PRE_USER_PROFILE_id = "id";
    private String PRE_USER_PROFILE_username = "username";
    private String PRE_USER_PROFILE_displayname = "d_name";
    private String PRE_USER_PROFILE_user_type = "user_type";
    private String PRE_USER_PROFILE_fname = "fname";
    private String PRE_USER_PROFILE_lname = "lname";
    private String PRE_USER_PROFILE_email = "email";


    //   private String PRE_USER_PROFILE_system_pwd= "system_pwd";

    private String PRE_USER_PROFILE_address = "address";
    private String PRE_USER_PROFILE_city = "city";
    private String PRE_USER_PROFILE_state = "state";
    private String PRE_USER_PROFILE_zip = "zip";
    private String PRE_USER_PROFILE_mobile = "mobile";

    private String PRE_USER_PROFILE_fax = "fax";
    private String PRE_USER_PROFILE_billing_addr = "billing_addr";
    private String PRE_USER_PROFILE_shipping_addr = "shipping_addr";


    private String PRE_USER_PROFILE_merchant_user_id = "merchant_user_id";


    //...................Getter method...............//

    public String getPRE_USER_PROFILE_id() {
        // return PRE_USER_PROFILE_id;
        return pref.getString(PRE_USER_PROFILE_id, "");
    }

    public String getPRE_USER_PROFILE_mobile() {
        // return PRE_USER_PROFILE_mobile;
        return pref.getString(PRE_USER_PROFILE_mobile, "");
    }

    public String getPRE_USER_PROFILE_zip() {
        // return PRE_USER_PROFILE_zip;
        return pref.getString(PRE_USER_PROFILE_zip, "");
    }

    public String getPRE_USER_PROFILE_state() {
        //  return PRE_USER_PROFILE_state;
        return pref.getString(PRE_USER_PROFILE_state, "");
    }

    public String getPRE_USER_PROFILE_city() {
        // return PRE_USER_PROFILE_city;
        return pref.getString(PRE_USER_PROFILE_city, "");
    }

    public String getPRE_USER_PROFILE_address() {
        //  return PRE_USER_PROFILE_address;
        return pref.getString(PRE_USER_PROFILE_address, "");
    }

    public String getPRE_USER_PROFILE_email() {
        // return PRE_USER_PROFILE_email;
        return pref.getString(PRE_USER_PROFILE_email, "");
    }

    public String getPRE_USER_PROFILE_lname() {
        // return PRE_USER_PROFILE_lname;
        return pref.getString(PRE_USER_PROFILE_lname, "");
    }

    public String getPRE_USER_PROFILE_fname() {
        // return PRE_USER_PROFILE_fname;
        return pref.getString(PRE_USER_PROFILE_fname, "");
    }

    public String getPRE_USER_PROFILE_user_type() {
        // return PRE_USER_PROFILE_user_type;
        return pref.getString(PRE_USER_PROFILE_user_type, "");
    }

    public String getPRE_USER_PROFILE_username() {
        //return PRE_USER_PROFILE_username;
        return pref.getString(PRE_USER_PROFILE_username, "");
    }

    public String getPRE_USER_PROFILE_shipping_addr() {
        // return PRE_USER_PROFILE_shipping_addr;
        return pref.getString(PRE_USER_PROFILE_shipping_addr, "");
    }

    public String getPRE_USER_PROFILE_fax() {
        //  return PRE_USER_PROFILE_fax;
        return pref.getString(PRE_USER_PROFILE_fax, "");
    }

    public String getPRE_USER_PROFILE_billing_addr() {
        //  return PRE_USER_PROFILE_billing_addr;
        return pref.getString(PRE_USER_PROFILE_billing_addr, "");
    }

//    public String getPRE_USER_PROFILE_system_pwd() {
//      //  return PRE_USER_PROFILE_system_pwd;
//        return pref.getString(PRE_USER_PROFILE_system_pwd, "");
//    }

    public String getPRE_USER_PROFILE_merchant_user_id() {
        // return PRE_USER_PROFILE_merchant_id;
        return pref.getString(PRE_USER_PROFILE_merchant_user_id, "");
    }

    public String getPRE_USER_PROFILE_displayname() {
        //return pref.PRE_USER_PROFILE_displayname;
        return pref.getString(PRE_USER_PROFILE_displayname, "");
    }


    //...................Setter method...............//

    public void setPRE_USER_PROFILE_id(String value) {
        //this.PRE_USER_PROFILE_id = PRE_USER_PROFILE_id;
        pref.edit().putString(PRE_USER_PROFILE_id, value).commit();
    }

    public void setPRE_USER_PROFILE_displayname(String value) {
        // this.PRE_USER_PROFILE_displayname = PRE_USER_PROFILE_displayname;
        pref.edit().putString(PRE_USER_PROFILE_displayname, value).commit();
    }


    public void setPRE_USER_PROFILE_mobile(String value) {
        // this.PRE_USER_PROFILE_mobile = PRE_USER_PROFILE_mobile;
        pref.edit().putString(PRE_USER_PROFILE_mobile, value).commit();
    }

    public void setPRE_USER_PROFILE_zip(String value) {
        // this.PRE_USER_PROFILE_zip = PRE_USER_PROFILE_zip;
        pref.edit().putString(PRE_USER_PROFILE_zip, value).commit();
    }

    public void setPRE_USER_PROFILE_state(String value) {
        // this.PRE_USER_PROFILE_state = PRE_USER_PROFILE_state;
        pref.edit().putString(PRE_USER_PROFILE_state, value).commit();
    }

    public void setPRE_USER_PROFILE_city(String value) {
        //this.PRE_USER_PROFILE_city = PRE_USER_PROFILE_city;
        pref.edit().putString(PRE_USER_PROFILE_city, value).commit();
    }

    public void setPRE_USER_PROFILE_address(String value) {
        // this.PRE_USER_PROFILE_address = PRE_USER_PROFILE_address;
        pref.edit().putString(PRE_USER_PROFILE_address, value).commit();
    }

    public void setPRE_USER_PROFILE_email(String value) {
        // this.PRE_USER_PROFILE_email = PRE_USER_PROFILE_email;
        pref.edit().putString(PRE_USER_PROFILE_email, value).commit();
    }

    public void setPRE_USER_PROFILE_lname(String value) {
        //this.PRE_USER_PROFILE_lname = PRE_USER_PROFILE_lname;
        pref.edit().putString(PRE_USER_PROFILE_lname, value).commit();
    }

    public void setPRE_USER_PROFILE_fname(String value) {
        //this.PRE_USER_PROFILE_fname = PRE_USER_PROFILE_fname;
        pref.edit().putString(PRE_USER_PROFILE_fname, value).commit();
    }

    public void setPRE_USER_PROFILE_user_type(String value) {
        //this.PRE_USER_PROFILE_user_type = PRE_USER_PROFILE_user_type;
        pref.edit().putString(PRE_USER_PROFILE_user_type, value).commit();
    }

    public void setPRE_USER_PROFILE_username(String value) {
        // this.PRE_USER_PROFILE_username = PRE_USER_PROFILE_username;
        pref.edit().putString(PRE_USER_PROFILE_username, value).commit();
    }

    public void setPRE_USER_PROFILE_shipping_addr(String value) {
        // this.PRE_USER_PROFILE_shipping_addr = PRE_USER_PROFILE_shipping_addr;
        pref.edit().putString(PRE_USER_PROFILE_shipping_addr, value).commit();
    }

    public void setPRE_USER_PROFILE_fax(String value) {
        //this.PRE_USER_PROFILE_fax = PRE_USER_PROFILE_fax;
        pref.edit().putString(PRE_USER_PROFILE_fax, value).commit();
    }

    public void setPRE_USER_PROFILE_billing_addr(String value) {
        //this.PRE_USER_PROFILE_billing_addr = PRE_USER_PROFILE_billing_addr;
        pref.edit().putString(PRE_USER_PROFILE_billing_addr, value).commit();
    }


//    public void setPRE_USER_PROFILE_system_pwd(String value) {
//       // this.PRE_USER_PROFILE_system_pwd = PRE_USER_PROFILE_system_pwd;
//        pref.edit().putString(PRE_USER_PROFILE_system_pwd, value).commit();
//    }

    public void setPRE_USER_PROFILE_merchant_user_id(String value) {
        // this.PRE_USER_PROFILE_merchant_id = PRE_USER_PROFILE_merchant_id;
        pref.edit().putString(PRE_USER_PROFILE_merchant_user_id, value).commit();
    }


    //        username
//                user_type
//        fname
//                lname
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
// email
//
//                address
//        city
//                state
//        zip
//                mobile


    private String PRE_USER_PROFILE_termsNCondition = "termsNCondition";
    private String PRE_USER_PROFILE_call_now_phone = "call_now_phone";
    private String PRE_USER_PROFILE_support_email = "support_email";
    private String PRE_USER_PROFILE_privacy_policy = "privacy_policy";


    public String getPRE_USER_PROFILE_privacy_policy() {
        //return PRE_USER_PROFILE_privacy_policy;
        return pref.getString(PRE_USER_PROFILE_privacy_policy, "");
    }

    public String getPRE_USER_PROFILE_termsNCondition() {
        // return PRE_USER_PROFILE_termsNCondition;
        return pref.getString(PRE_USER_PROFILE_termsNCondition, "");
    }

    public String getPRE_USER_PROFILE_call_now_phone() {
        // return PRE_USER_PROFILE_call_now_phone;
        return pref.getString(PRE_USER_PROFILE_call_now_phone, "");
    }

    public String getPRE_USER_PROFILE_support_email() {
        //return PRE_USER_PROFILE_support_email;
        return pref.getString(PRE_USER_PROFILE_support_email, "");
    }


    public void setPRE_USER_PROFILE_termsNCondition(String value) {
        //this.PRE_USER_PROFILE_termsNCondition = PRE_USER_PROFILE_termsNCondition;
        pref.edit().putString(PRE_USER_PROFILE_termsNCondition, value).commit();


    }

    public void setPRE_USER_PROFILE_call_now_phone(String value) {
        //this.PRE_USER_PROFILE_call_now_phone = PRE_USER_PROFILE_call_now_phone;
        pref.edit().putString(PRE_USER_PROFILE_call_now_phone, value).commit();
    }

    public void setPRE_USER_PROFILE_support_email(String value) {
        //this.PRE_USER_PROFILE_support_email = PRE_USER_PROFILE_support_email;
        pref.edit().putString(PRE_USER_PROFILE_support_email, value).commit();
    }

    public void setPRE_USER_PROFILE_privacy_policy(String value) {
        // this.PRE_USER_PROFILE_privacy_policy = PRE_USER_PROFILE_privacy_policy;
        pref.edit().putString(PRE_USER_PROFILE_privacy_policy, value).commit();
    }

}
