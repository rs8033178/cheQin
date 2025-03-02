package com.cheqin.booking.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.cheqin.booking.MainActivity;

public class ShareUtils {

    public static void callPhone(Context context,String phone){
        if(phone!=null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else{
            showToast(context,"Invalid Phone Number!");
        }
    }
    public static void sendSMS(Context context,String phone){
        showToast(context,"Invalid Pho !"+phone);
        if(phone!=null && !phone.equals("") && !phone.equals("null")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("smsto:"));
            i.setType("vnd.android-dir/mms-sms");
            i.putExtra("address", new String(phone));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.putExtra("sms_body","Utel");
            context.startActivity(Intent.createChooser(i, "Send sms via:"));
        }else{
            showToast(context,"Phone Number unavailable!");
        }
    }

    public static void sendEmail(Context context,String email){
        if(email!=null) {
            Log.i("Send email", "");
            String[] TO = {email};
            String[] CC = {""};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Utel]");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            } catch (android.content.ActivityNotFoundException ex) {
                showToast(context, "There is no email client installed.");
            }
        }else{
            showToast(context, "Invalid emailId!");
        }
    }
public  static void sendWhatsApp(Context context,String phone){
if(phone!=null) {
    try {


        String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code.
        //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + phone + "?body=" + ""));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.setPackage("com.whatsapp");
        context.startActivity(sendIntent);
    } catch (Exception e) {
        e.printStackTrace();
        showToast(context, "WhatsApp not Installed!");
    }
}else{
    showToast(context, "Invalid Phone Number!");
}
}

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
}
