package com.cheqin.booking.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Pair;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.crashlytics.android.Crashlytics;
import com.cheqin.booking.MainActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by VaibhaV on 16-Dec-15.
 */
public class Common {

    public static Date convertStringToDate(String dateString) {
        // String dtStart = "2010-10-15T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static final Pattern EMAIL_ADDRESS = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");


    public static boolean isValidEmail(String email_id) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email_id).matches();
    }


    public static boolean isNetworkAvailable(Context mContext) {

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void ExitAppDialog(final Activity activity) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setMessage("Exit Application?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.finishAffinity();
                        //activity.finish();
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.show();
    }

    public static void doLogoutOperation(Context context) {
        //showToast(context,Constants.INVALID_EMAIL_PASS);
        clearDataFromSharedPrefernce(context);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private static void clearDataFromSharedPrefernce(Context context) {
        // SharedPreferences.Editor edit = pref.edit();
//                        edit.putBoolean("login_old", false);
//                        edit.putString("auth_token", "");
//                        edit.putString("password", "");
//                        edit.putString("usertype","");
//                        edit.commit();
        SettingSharedPrefs ssp = SettingSharedPrefs.getInstance(context.getApplicationContext());
        ssp.clearSharedPreference();
    }

    public static String getCurrencySymbol(String currencyCode) {
        try {
            Currency currencySymbol = Currency.getInstance(currencyCode);
            return currencySymbol.getSymbol();
        } catch (IllegalArgumentException ille) {
            //Crashlytics.log(currencyCode + ille.getMessage());
        }
        return currencyCode;
    }

    public static void logException(Throwable e) {
        Crashlytics.logException(e);
    }

    public static int calculateDateDiff(long startMillis, long endMillis) {
        try {
            int totalNights = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(endMillis - startMillis));
            return totalNights;
        } catch (Exception e) {
            logException(e);
        }

        return 0;

    }

    public static String getFormattedAmount(String s) {
        try {
            double amount = Double.parseDouble(s);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String result = decimalFormat.format(amount);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    public static String getFormattedAmount(double amount) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return decimalFormat.format(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}
