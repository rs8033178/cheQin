package com.cheqin.booking.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.cheqin.booking.Bean.RoomTypeBean;
import com.cheqin.booking.Bean.Userbean;
import com.cheqin.booking.R;
import com.cheqin.booking.gcm.SharedPreferenceRoom;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class UserLiveListAdapter extends ArrayAdapter<Userbean> {
    private List<Userbean> Userbean = null;
    private Context context = null;
    private AsyncTaskListener listener = null;
    private LayoutInflater inflater = null;
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
    private List<RoomTypeBean> roomTypeBeanList;

    public UserLiveListAdapter(Context context, List<Userbean> rowItems) {
        super(context, R.layout.user_live_requests_items, rowItems);
//        Animation animation = null;
//        animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
        this.context = context;
//        this.rowItems = rowItems;
        Userbean = rowItems;

        SharedPreferenceRoom sharedPreferenceRoom = new SharedPreferenceRoom();
        roomTypeBeanList = sharedPreferenceRoom.getFavorites(context);
//        this.listener = (AsyncTaskListener) UserLiveListAdapter.this;
//        this.rowItems = rowItems;
//        inflater = this.context.getSystemService(context.user_live_requests_items);
    }

    private class ViewHolder {
        TextView txt_checkinday;
        TextView txt_checkinmonth;
        TextView txt_checkinyear;
        TextView txt_checkoutday;
        TextView txt_checkoutmonth;
        TextView txt_checkoutyear;
        TextView Rooms;
        TextView Adults;
        TextView Childrens;
        TextView Locality;
        TextView Hotel_type;
        TextView offered;
        TextView chkin_hh;
        TextView chkout_hh;
        LinearLayout offir;
        TextView roomType;
        CardView container;

        LinearLayout exhibitionContainer, hotelBookingContainer;
        TextView exhibitionGuests, exhibitionBudget, exhibitionCategory, exhibitionCity, exhibitionOffers;
        TextView exhibitionCheckin, exhibitionCheckin1, exhibitionCheckin2;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.user_live_requests_items, null);
            holder = new ViewHolder();

            holder.txt_checkinday = (TextView) rowView.findViewById(R.id.uckin_date);
            holder.container = rowView.findViewById(R.id.cb_container);
            holder.txt_checkinmonth = (TextView) rowView.findViewById(R.id.uckin_month);
            holder.txt_checkinyear = (TextView) rowView.findViewById(R.id.uckin_year);
            holder.txt_checkoutday = (TextView) rowView.findViewById(R.id.uckout_date);
            holder.txt_checkoutmonth = (TextView) rowView.findViewById(R.id.uckout_month);
            holder.txt_checkoutyear = (TextView) rowView.findViewById(R.id.uckout_year);
            holder.Rooms = (TextView) rowView.findViewById(R.id.user_rooms);
            holder.Adults = (TextView) rowView.findViewById(R.id.user_adults);
            holder.Childrens = (TextView) rowView.findViewById(R.id.user_childrens);
            holder.Locality = (TextView) rowView.findViewById(R.id.user_locality);
            holder.offered = rowView.findViewById(R.id.see_offers);
            holder.chkin_hh = (TextView) rowView.findViewById(R.id.chkin_hh);
            holder.chkout_hh = (TextView) rowView.findViewById(R.id.chkout_hh);
            holder.roomType = (TextView) rowView.findViewById(R.id.roomType);

            holder.exhibitionContainer = rowView.findViewById(R.id.ll_exhibition_container);
            holder.hotelBookingContainer = rowView.findViewById(R.id.hotel_booking_container);
            holder.exhibitionCheckin = rowView.findViewById(R.id.exhibition_checkin_date);
            holder.exhibitionCheckin1 = rowView.findViewById(R.id.exhibition_checkin_date1);
            holder.exhibitionCheckin2 = rowView.findViewById(R.id.exhibition_checkin_date2);
            holder.exhibitionGuests = rowView.findViewById(R.id.tv_exhibtion_guest_count);
            holder.exhibitionBudget = rowView.findViewById(R.id.tv_exhibition_budget);
            holder.exhibitionCategory = rowView.findViewById(R.id.tv_exhibition_category);
            holder.exhibitionCity = rowView.findViewById(R.id.tv_exhibition_locality);
            holder.exhibitionOffers = rowView.findViewById(R.id.tv_exhibition_see_offers);

            rowView.setTag(holder);

        } else {
            holder = (ViewHolder) rowView.getTag();
        }


        String daycheckin = null, monthcheckin = null, yearcheckin = null, daycheckout = null, monthcheckout = null, yearcheckout = null;
        final Userbean bn = Userbean.get(position);

        if (bn.isExhibition()) {
            holder.exhibitionContainer.setVisibility(View.VISIBLE);
            holder.hotelBookingContainer.setVisibility(View.GONE);

            holder.exhibitionBudget.setText("Venue Budget: " + context.getString(R.string.rupee_amount, bn.getPrice()));
            holder.exhibitionCity.setText(bn.getHotel_type());
            holder.exhibitionCheckin.setText(bn.getCheck_in_date());
            holder.exhibitionGuests.setText(bn.getAdults());
            holder.exhibitionCategory.setText(bn.getExhibitionCategoryName());

            if (!TextUtils.isEmpty(bn.getCheck_in_date1()) && !"null".equalsIgnoreCase(bn.getCheck_in_date1())) {
                holder.exhibitionCheckin1.setText(bn.getCheck_in_date1());
                holder.exhibitionCheckin1.setVisibility(View.VISIBLE);
            } else {
                holder.exhibitionCheckin1.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(bn.getCheck_in_date2()) && !"null".equalsIgnoreCase(bn.getCheck_in_date2())) {
                holder.exhibitionCheckin2.setText(bn.getCheck_in_date2());
                holder.exhibitionCheckin2.setVisibility(View.VISIBLE);
            } else {
                holder.exhibitionCheckin2.setVisibility(View.GONE);
            }

            if (bn.getOffered().equalsIgnoreCase("No Offer(s) Yet!!")) {
                holder.exhibitionOffers.setText(bn.getOffered());
            } else {
                holder.exhibitionOffers.setText(bn.getOffered());
            }

            if (bn.isActionEnabled() && !bn.isExpired()) {
                holder.exhibitionOffers.setBackgroundColor(ContextCompat.getColor(context, R.color.utel_color));
            } else {
                holder.exhibitionOffers.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccentLight));
            }

        } else {

            holder.exhibitionContainer.setVisibility(View.GONE);
            holder.hotelBookingContainer.setVisibility(View.VISIBLE);

            holder.offered.setEnabled(bn.isActionEnabled() && !bn.isExpired());

            if (bn.getShort_stay().equalsIgnoreCase("0")) {
                daycheckin = bn.getCheck_in_date().substring(8, 10);
                monthcheckin = bn.getCheck_in_date().substring(5, 7);
                yearcheckin = bn.getCheck_in_date().substring(0, 4);
                holder.txt_checkinday.setText(daycheckin);
                holder.txt_checkinyear.setText(yearcheckin);
                Date checkInMonth = new Date();
                checkInMonth.setMonth(Integer.parseInt(monthcheckin) - 1);
                holder.txt_checkinmonth.setText(monthFormat.format(checkInMonth));

                if (!TextUtils.isEmpty(bn.getCheck_out_date()) && !bn.getCheck_out_date().equals("null")) {
                    daycheckout = bn.getCheck_out_date().substring(8, 10);
                    monthcheckout = bn.getCheck_out_date().substring(5, 7);
                    yearcheckout = bn.getCheck_out_date().substring(0, 4);
                }
                holder.txt_checkoutday.setText(daycheckout);
                holder.txt_checkoutyear.setText(yearcheckout);

                try {
                    Date checkoutMonth = new Date();
                    checkoutMonth.setMonth(Integer.parseInt(monthcheckout) - 1);
                    holder.txt_checkoutmonth.setText(monthFormat.format(checkoutMonth));
                } catch (Exception ignored) {

                }

                holder.chkin_hh.setText("12:00");
                holder.chkout_hh.setText("11:00");

            } else if (bn.getShort_stay().equalsIgnoreCase("1")) {

                daycheckin = bn.getCheck_in_date().substring(8, 10);
                monthcheckin = bn.getCheck_in_date().substring(5, 7);
                yearcheckin = bn.getCheck_in_date().substring(0, 4);

                Date checkInMonth = new Date();
                checkInMonth.setMonth(Integer.parseInt(monthcheckin) - 1);
                holder.txt_checkinmonth.setText(monthFormat.format(checkInMonth));

                holder.chkin_hh.setText(bn.getCheck_in_date().substring(11, 16));
                holder.txt_checkinday.setText(daycheckin);
                holder.txt_checkinyear.setText(yearcheckin);

                if (bn.getCheck_out_date() != null && !bn.getCheck_out_date().equals("null")) {
                    daycheckout = bn.getCheck_out_date().substring(8, 10);
                    monthcheckout = bn.getCheck_out_date().substring(5, 7);
                    yearcheckout = bn.getCheck_out_date().substring(0, 4);

                    holder.chkout_hh.setText(bn.getCheck_out_date().substring(11, 16));
                }


                holder.txt_checkoutday.setText(daycheckout);
                holder.txt_checkoutyear.setText(yearcheckout);
                Date checkoutMonth = new Date();
                checkoutMonth.setMonth(Integer.parseInt(monthcheckout) - 1);
                holder.txt_checkoutmonth.setText(monthFormat.format(checkoutMonth));
            }

            holder.Rooms.setText(bn.getRooms());
            holder.Adults.setText(bn.getAdults());
            holder.Childrens.setText(bn.getChildrens());
            holder.Locality.setText(bn.getHotel_type());
            for (int i = 0; i < roomTypeBeanList.size(); i++) {
                if (roomTypeBeanList.get(i).getId().equals(bn.getRoom_type_id())) {
                    //String s = "<b>Selected Room : </b>"+roomTypeBeanList.get(i).getName();
                    holder.roomType.setText("Room Type: " + roomTypeBeanList.get(i).getName());
                    // holder.roomType.setText(Html.fromHtml(s));
                }
            }

            if (bn.getOffered().equalsIgnoreCase("No Offer(s) Yet!!")) {
                holder.offered.setText(bn.getOffered());
            } else {
                holder.offered.setText(bn.getOffered());
            }

            if (bn.isActionEnabled() && !bn.isExpired()) {
                holder.offered.setBackgroundColor(ContextCompat.getColor(context, R.color.utel_color));
            } else {
                holder.offered.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccentLight));
            }
        }

        return rowView;

    }

    public int getDays(String daycheckin, String checkinmon, String yearcheckin, String daycheckout, String checkoutmon, String yearcheckout) {
        try {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            //Date dateout = new SimpleDateFormat("mm", Locale.ENGLISH).parse(checkoutmon);
            //Date datein = new SimpleDateFormat("mm", Locale.ENGLISH).parse(checkinmon);

            //Calendar calout = Calendar.getInstance();
            //calout.setTime(dateout);
            //Calendar calin = Calendar.getInstance();
            //calin.setTime(datein);

            cal1.set(Integer.parseInt(yearcheckin), Integer.parseInt(checkinmon) + 1, Integer.parseInt(daycheckin));
            cal2.set(Integer.parseInt(yearcheckout), Integer.parseInt(checkoutmon) + 1, Integer.parseInt(daycheckout));
            return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis()));

        } catch (Exception e) {
            e.printStackTrace();
            Common.logException(e);
        }
        return 0;

    }

}
