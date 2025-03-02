package com.cheqin.booking.mappager;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.MapViewPager;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.cheqin.booking.R;
import com.cheqin.booking.mappager.model.TopFiveLiveOffer;
import com.cheqin.booking.mappager.model.TopOffersResponse;
import com.cheqin.booking.views.WrappingViewPager;

import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MapTopOfferPagerAdapter extends MapViewPager.Adapter {

    private List<TopFiveLiveOffer> topFiveLiveOffers;
    private TopOffersResponse topOffersResponse;
    private Location location;
    private int mCurrentPosition = -1;
    private MapPagerFragment fragment;


    public MapTopOfferPagerAdapter(FragmentManager fm, List<TopFiveLiveOffer> topFiveLiveOffers, TopOffersResponse topOffersResponse, Context mContext, Location location) {
        super(fm);
        this.topFiveLiveOffers = topFiveLiveOffers;
        this.topOffersResponse = topOffersResponse;
        this.location = location;
        this.mContext = mContext;
    }

    private Context mContext;

    @Override
    public int getCount() {
        return topFiveLiveOffers.size();
    }

    @Override
    public Fragment getItem(int position) {
      fragment  =   MapPagerFragment.newInstance(position, topFiveLiveOffers.get(position), topOffersResponse, location);
      return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String category = topFiveLiveOffers.get(position).getHotelCategory();
        return topFiveLiveOffers.get(position).getUserBusinessName() + "  (" + category + ")";
    }

    @Override
    public CameraPosition getCameraPosition(int position) {
        // Just a helper function to obtain the device's display size in px


// The two fixed points: Bottom end of the map & Top end of the map,
// both centered horizontally (because I want to offset the camera vertically...
// if you want to offset it to the right, for instance, use the left & right ends)
        if (topFiveLiveOffers != null && topFiveLiveOffers.size() > 0)
            return CameraPosition.fromLatLngZoom(new LatLng(Double.parseDouble(topFiveLiveOffers.get(position).getHotelLatitude()),
                    Double.parseDouble(topFiveLiveOffers.get(position).getHotelLongitude())), 15f);
        return null;
    }

    @Override
    public View getMarkerView(int position) {
        View marker = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        TextView tv = marker.findViewById(R.id.txt_marker);
        String currencyCode = topFiveLiveOffers.get(position).getCurrencyCode() == null || topFiveLiveOffers.get(position).getCurrencyCode().equals("") ? "INR" : topFiveLiveOffers.get(position).getCurrencyCode();

        tv.setText(String.format("%s%d", Common.getCurrencySymbol(currencyCode), topFiveLiveOffers.get(position).getOfferPrice()));
        return marker;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
//            Fragment fragment = (Fragment) object;
            WrappingViewPager pager = (WrappingViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.requireView());
            }
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
