package com.cheqin.booking.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cheqin.booking.Bean.UserMatchesbean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.VolleySingleton;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UserMatchesListAdapter extends ArrayAdapter<UserMatchesbean> {
    private List<UserMatchesbean> UserMatchesbean = null;
    private Context context1 = null;
    private AsyncTaskListener listener1 = null;
    private LayoutInflater inflater = null;
    private HashMap<TextView,CountDownTimer> counters;
    DecimalFormat commaformatter;


    public UserMatchesListAdapter(Context context1, List<UserMatchesbean> rowItems1, DecimalFormat commaformatter) {
        super(context1, R.layout.user_show_matches_listitems, rowItems1);
        this.context1 = context1;
        UserMatchesbean = rowItems1;
        this.counters = new HashMap<TextView, CountDownTimer>();
        this.commaformatter = commaformatter;

    }
    private class ViewHolder {
        TextView HotelName;
        TextView HotelAddress;
        TextView TotalPrice;
        TextView HotelCity;
        TextView Distance;
        TextView tvCounter;
        NetworkImageView image;



    }

    @Override
    public int getCount() {
        return 3;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder1 = null;
        inflater = (LayoutInflater) this.context1.getSystemService(context1.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.user_show_matches_listitems, null);
            holder1 = new ViewHolder();
            holder1.HotelName = (TextView) rowView.findViewById(R.id.hotelname);
            holder1.HotelAddress = (TextView) rowView.findViewById(R.id.hoteladdress);
            holder1.TotalPrice = (TextView) rowView.findViewById(R.id.price);
            holder1.HotelCity = (TextView) rowView.findViewById(R.id.hotelcity);
            holder1.Distance = (TextView) rowView.findViewById(R.id.distance);
            holder1.tvCounter = (TextView) rowView.findViewById(R.id.expiry_time);
            holder1.image = (NetworkImageView) rowView.findViewById(R.id.networkimage1);
            rowView.setTag(holder1);

        }else
            holder1 = (ViewHolder) rowView.getTag();

        final UserMatchesbean bn = UserMatchesbean.get(position);
//        holder.Item_info.setText(bn.setItem_info());
        holder1.HotelName.setText(bn.getHotelName());
        holder1.HotelAddress.setText(bn.getHotelAddress());
        holder1.TotalPrice.setText(bn.getCurrency()+" "+commaformatter.format(Integer.valueOf(bn.getTotalPrice())));
        holder1.HotelCity.setText(bn.getHotelCity());
        holder1.Distance.setText(bn.getDistance());
        holder1.image.setImageUrl(bn.getImage(), VolleySingleton.getInstance(context1).getImageLoader());
        timerPart(rowView, position, holder1);

        return rowView;
    }

    private void timerPart(View rowView,int position,ViewHolder holder) {

        final TextView tv = holder.tvCounter;

        CountDownTimer cdt = counters.get(holder.tvCounter);
        if(cdt!=null)
        {
            cdt.cancel();
            cdt=null;
        }
        Date date = UserMatchesbean.get(position).getDate();
        long currentDate = Calendar.getInstance().getTime().getTime();
        long limitDate =date.getTime();
        long difference = limitDate - currentDate;
        cdt = new CountDownTimer(difference, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                int days = 0;
                int hours = 0;
                int minutes = 0;
                int seconds = 0;
                String sDate = "";

                if(millisUntilFinished > DateUtils.DAY_IN_MILLIS)
                {
                    days = (int) (millisUntilFinished / DateUtils.DAY_IN_MILLIS);
                    sDate += days+"D";
                }

                millisUntilFinished -= (days*DateUtils.DAY_IN_MILLIS);

                if(millisUntilFinished > DateUtils.HOUR_IN_MILLIS)
                {
                    hours = (int) (millisUntilFinished / DateUtils.HOUR_IN_MILLIS);
                }

                millisUntilFinished -= (hours*DateUtils.HOUR_IN_MILLIS);

                if(millisUntilFinished > DateUtils.MINUTE_IN_MILLIS)
                {
                    minutes = (int) (millisUntilFinished / DateUtils.MINUTE_IN_MILLIS);
                }

                millisUntilFinished -= (minutes*DateUtils.MINUTE_IN_MILLIS);

                if(millisUntilFinished > DateUtils.SECOND_IN_MILLIS)
                {
                    seconds = (int) (millisUntilFinished / DateUtils.SECOND_IN_MILLIS);
                }

                sDate += " "+String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
                tv.setText(sDate.trim());
            }

            @Override
            public void onFinish() {
                tv.setText("Finished");
            }

        };

        counters.put(tv, cdt);
        cdt.start();

    }

    public void cancelAllTimers()
    {
        Set<Map.Entry<TextView, CountDownTimer>> s = counters.entrySet();
        Iterator it = s.iterator();
        while(it.hasNext())
        {
            try
            {
                Map.Entry pairs = (Map.Entry)it.next();
                CountDownTimer cdt = (CountDownTimer)pairs.getValue();

                cdt.cancel();
                cdt = null;
            }
            catch(Exception e){}
        }
        it=null;
        s=null;
        counters.clear();
    }
}