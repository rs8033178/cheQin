package com.cheqin.booking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cheqin.booking.Bean.Confirm_Bean;
import com.cheqin.booking.R;
import com.cheqin.booking.User.UserConfirmBooking;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.CustomFontTextview;
import com.cheqin.booking.utils.ShareUtils;

import java.text.SimpleDateFormat;
import java.util.List;


public class UserConfirmBookingAdapter extends ArrayAdapter<Confirm_Bean> {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 100;
    Context context;
    private List<Confirm_Bean> confirm_bean = null;
    LayoutInflater mInflater;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat newfmt = new SimpleDateFormat("dd MMM yyyy");
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private UserConfirmBooking asyncTaskListener = null;

    public UserConfirmBookingAdapter(Context context, List<Confirm_Bean> rowitems, UserConfirmBooking asyncTaskListener) {
        super(context, R.layout.user_confirm_booking_items, rowitems);
        this.context = context;
        confirm_bean = rowitems;
        this.asyncTaskListener = asyncTaskListener;
    }

    private class ViewHolder {
        CustomFontTextview btn_cancel;
        TextView txt_room_type;
        TextView txt_price;
        TextView txt_chk_in;
        TextView txt_chk_out;
        TextView confirmed_hotel_address, confirmed_hotel_type, confirmed_hotel_name;
        Button btn;
        ImageButton callIB, smsIB, emailIB;
        ImageView whatappIB, uberIB, directionIB;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        mInflater = (LayoutInflater) this.context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_confirm_booking_items, null);
            holder.txt_price = (TextView) convertView.findViewById(R.id.confirmed_hotel_price);
            holder.txt_room_type = convertView.findViewById(R.id.confirmed_hotel_room_type);
            holder.confirmed_hotel_type = (TextView) convertView.findViewById(R.id.confirmed_hotel_type);
            holder.confirmed_hotel_name = (TextView) convertView.findViewById(R.id.confirmed_hotel_name);
            holder.confirmed_hotel_address = (TextView) convertView.findViewById(R.id.confirmed_hotel_address);
            holder.txt_chk_in = (TextView) convertView.findViewById(R.id.chk_in);
            holder.txt_chk_out = (TextView) convertView.findViewById(R.id.chk_out);
            holder.emailIB = (ImageButton) convertView.findViewById(R.id.emailIB);
            holder.callIB = (ImageButton) convertView.findViewById(R.id.callIB);
            holder.smsIB = (ImageButton) convertView.findViewById(R.id.smsIB);
            holder.whatappIB = (ImageView) convertView.findViewById(R.id.whatappIB);
            holder.uberIB = (ImageView) convertView.findViewById(R.id.uberIB);
            holder.directionIB = (ImageView) convertView.findViewById(R.id.directionIB);
            holder.btn_cancel = (CustomFontTextview) convertView.findViewById(R.id.btn_cancel);
//            holder.btn = (Button) convertView.findViewById(R.id.button5);
//            holder.btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "dfdfgbfd", Toast.LENGTH_LONG).show();
//                }
//            });
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.confirmed_hotel_type.setText(confirm_bean.get(position).getHotel_class());
        holder.confirmed_hotel_name.setText(confirm_bean.get(position).getHotel_name());
        holder.confirmed_hotel_address.setText(confirm_bean.get(position).getAddress());

        String price = confirm_bean.get(position).getHotel_price();
        if (price != null && price.contains(".")) {
            holder.txt_price.setText("Bargained Offer : " + Common.getCurrencySymbol(confirm_bean.get(position).getCurrency()) + (int) Math.floor(Double.parseDouble(price)));
        } else {
            holder.txt_price.setText("Bargained Offer : " + Common.getCurrencySymbol(confirm_bean.get(position).getCurrency()) + price);
        }
        try {
            holder.txt_chk_in.setText("Check in\n" + newfmt.format(fmt.parse(confirm_bean.get(position).getCheck_in_date())));
            holder.txt_chk_out.setText("Check out\n" + newfmt.format(fmt.parse(confirm_bean.get(position).getCheck_out_date())));

        } catch (Exception e) {
            e.printStackTrace();
            Common.logException(e);
            holder.txt_chk_in.setText("Check in\n" + "Not Found!");
            holder.txt_chk_out.setText("Check out\n" + "Not Found!");
        }
        holder.txt_room_type.setText("Room Type : " + confirm_bean.get(position).getOffered_room_type());

        String email = confirm_bean.get(position).getHotelier_email();
        if (email != null && !email.equals("") && !email.equals("null") && ShareUtils.isValidEmail(email)) {
            holder.emailIB.setOnClickListener(view -> {
                ShareUtils.sendEmail(asyncTaskListener, confirm_bean.get(position).getHotelier_email());
            });
        } else {
            holder.emailIB.setColorFilter(context.getResources().getColor(R.color.gray_btn_bg_color));
        }
        String phone = confirm_bean.get(position).getHotelier_mobile();
        if (phone != null && !phone.equals("") && !phone.equals("null") && ShareUtils.isValidPhoneNumber(phone)) {
            holder.callIB.setOnClickListener(view -> {
                ShareUtils.callPhone(asyncTaskListener, confirm_bean.get(position).getHotelier_mobile());
            });
            holder.smsIB.setOnClickListener(view -> {
                ShareUtils.sendSMS(asyncTaskListener, confirm_bean.get(position).getHotelier_mobile());
            });
            holder.whatappIB.setOnClickListener(view -> {
                ShareUtils.sendWhatsApp(asyncTaskListener, confirm_bean.get(position).getHotelier_mobile());
            });
            holder.uberIB.setOnClickListener(view -> {
                launchUBER(position);
            });
            holder.directionIB.setOnClickListener(view -> {
                navigateToHotel(position);
            });
        } else {
            holder.callIB.setColorFilter(context.getResources().getColor(R.color.gray_btn_bg_color));
            holder.smsIB.setColorFilter(context.getResources().getColor(R.color.gray_btn_bg_color));
            holder.whatappIB.setColorFilter(context.getResources().getColor(R.color.gray_btn_bg_color));
        }
        holder.btn_cancel.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(asyncTaskListener);
            alertDialogBuilder.setTitle("Cancel")
                    .setMessage("Are you sure you want to Cancel the Booking?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> asyncTaskListener.checkConnection("cancel", position))
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();
        });
        return convertView;
    }

    private void launchUBER(int position) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String dropOff = "&dropoff[latitude]=" + confirm_bean.get(position).getHotel_latitude() + "&dropoff[longitude]=" + confirm_bean.get(position).getHotel_longitude() + "";

            String uri = "uber://?action=setPickup&pickup=my_location" + dropOff;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
            }
        }
    }

    private void navigateToHotel(int position) {
        try {
            Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + confirm_bean.get(position).getRequestLatitude() + "," + confirm_bean.get(position).getRequestLongitude() + "&daddr=" + confirm_bean.get(position).getHotel_latitude() + "," + confirm_bean.get(position).getHotel_longitude());

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Route Not Found!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
