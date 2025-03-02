package com.cheqin.booking.mappager;

import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.TextUtilsCompat;
import androidx.databinding.DataBindingUtil;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.cheqin.booking.Bean.RoomTypeBean;
import com.cheqin.booking.R;
import com.cheqin.booking.databinding.FragmentMapPagerBinding;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.gcm.SharedPreferenceRoom;
import com.cheqin.booking.mappager.model.TopFiveLiveOffer;
import com.cheqin.booking.mappager.model.TopOffersResponse;
import com.cheqin.booking.modelplaces.GooglePlacesResponse;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MapPagerFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private int index;
    private TopFiveLiveOffer topOffer;
    private TopOffersResponse topOffersResponse;
    private GooglePlacesResponse response;
    private FragmentMapPagerBinding mDataBinding;
    private Progressloadingdialog progressloadingdialog;
    private double latitude;
    private double longitude;
    String browserKey;
    private List<RoomTypeBean> roomTypeBeanList;

    public MapPagerFragment() {
    }

    public static MapPagerFragment newInstance(int i, TopFiveLiveOffer offer, TopOffersResponse response, Location location) {
        Gson gson = new Gson();
        MapPagerFragment f = new MapPagerFragment();
        Bundle args = new Bundle();
        args.putInt("INDEX", i);
        args.putString("offer", gson.toJson(offer, TopFiveLiveOffer.class));
        args.putString("topOffersResponse", gson.toJson(response, TopOffersResponse.class));

        if (location != null) {
            args.putDouble("lat", location.getLatitude());
            args.putDouble("long", location.getLongitude());
        }

        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            Gson gson = new Gson();
            index = args.getInt("INDEX", 0);
            topOffer = gson.fromJson(args.getString("offer"), TopFiveLiveOffer.class);
            topOffersResponse = gson.fromJson(args.getString("topOffersResponse"), TopOffersResponse.class);
            latitude = args.getDouble("lat", 0.0);
            longitude = args.getDouble("long", 0.0);
            SharedPreference sharedPreference = new SharedPreference();
            browserKey = sharedPreference.getApiKey(getActivity());
            SharedPreferenceRoom sharedPreferenceRoom = new SharedPreferenceRoom();
            roomTypeBeanList = sharedPreferenceRoom.getFavorites(getContext());
            //https://www.journaldev.com/13911/google-places-api
            String url = "https://maps.googleapis.com/maps/api/place/details/json?";

            HashMap<String, String> params = new HashMap<>();
            params.put("key", browserKey);
            params.put("placeid", topOffer.getG_place_id());
            params.put("fields", "review,photo,name,rating,formatted_address,formatted_phone_number,user_ratings_total");
            //  Toast.makeText(getContext(),""+topOffer.getPlaceId(),Toast.LENGTH_SHORT).show();

            progressloadingdialog = new Progressloadingdialog(getActivity(), "Please Wait...");
            HttpAsync httpAsync = new HttpAsync(getActivity(), listener, url, params, 1, "googleapi");
            progressloadingdialog.show();
            httpAsync.execute();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding.hotelName.setText("" + topOffer.getUserBusinessName());
        mDataBinding.hotelAddress.setText(""+topOffer.getOnbehalfHotelName());

        if (TextUtils.isEmpty(topOffersResponse.getRequestDetail().getCategoryName()))
            mDataBinding.hotelType.setVisibility(View.GONE);
        else {
            mDataBinding.hotelType.setVisibility(View.VISIBLE);
            mDataBinding.hotelType.setText(String.format("(%s)", topOffer.getHotelCategory()));
        }
    }

    private String emptyifnull(String text) {
        if (text == null || "".equals(text) || "null".equalsIgnoreCase(text)) {
            return "";
        } else
            return text;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_pager, container, false);
        mDataBinding.setTopFiveLiveOffer(topOffer);
        mDataBinding.setOnclick(this);
        mDataBinding.seeDetails.setOnClickListener(v -> {
            gotToDetails();
        });
        mDataBinding.hotelAddress.setMovementMethod(new ScrollingMovementMethod());
        mDataBinding.launchUber.setOnClickListener(v -> {
            PackageManager pm = getActivity().getPackageManager();
            try {
                pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                String dropOff = "&dropoff[latitude]=" + topOffer.getHotelLatitude() + "&dropoff[longitude]=" + topOffer.getHotelLongitude() + "&dropoff[nickname]=" + response.getResult().getName() + "&dropoff[formatted_address]=" + response.getResult().getFormattedAddress() + "";
                String uri = "uber://?action=setPickup&pickup=my_location" + dropOff;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            } catch (PackageManager.NameNotFoundException e) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
                }
            }
        });

        mDataBinding.navigateToHotel.setOnClickListener(v -> {

            // Uri gmmIntentUri = Uri.parse("google.navigation:q=" + topOffer.getHotelLatitude() + "," + topOffer.getHotelLongitude() + "");
            try {
                // Uri gmmIntentUri =Uri.parse("google.navigation:q="+topOffersResponse.getRequestDetail().getLatitude()+","+topOffersResponse.getRequestDetail().getLongitude()+";"+topOffer.getHotelLatitude()+","+topOffer.getHotelLongitude());
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + topOffersResponse.getRequestDetail().getLatitude() + "," + topOffersResponse.getRequestDetail().getLongitude() + "&daddr=" + topOffer.getHotelLatitude() + "," + topOffer.getHotelLongitude());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Route Not Found!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        /*if (latitude == 0.0 && longitude == 0.0) {

            SharedPreferences sp = getActivity().getSharedPreferences("currentlocation", MODE_PRIVATE);
            try {
                latitude = Double.valueOf(sp.getString("current_latitude", "12.9716"));
                longitude = Double.valueOf(sp.getString("current_longitude", "12.9716"));
            } catch (Exception e) {
                latitude = 12.9716;
                longitude = 77.5946;
            }

        }*/
        try {
            latitude = topOffersResponse.getRequestDetail().getLatitude() != null ? Double.parseDouble(topOffersResponse.getRequestDetail().getLatitude()) : 0;
            longitude = topOffersResponse.getRequestDetail().getLongitude() != null ? Double.parseDouble(topOffersResponse.getRequestDetail().getLongitude()) : 0;
            Location selectedLocation = new Location("locationA");
            selectedLocation.setLongitude(longitude);
            selectedLocation.setLatitude(latitude);

            Location nearLocations = new Location("locationB");
            double offerLat = 0;
            double offerLong = 0;

            offerLat = Double.valueOf(topOffer.getHotelLatitude());
            offerLong = Double.valueOf(topOffer.getHotelLongitude());

            nearLocations.setLatitude(offerLat);
            nearLocations.setLongitude(offerLong);

            double distance = selectedLocation.distanceTo(nearLocations) / 1000;
            String dist = String.valueOf(distance);
            dist = dist.substring(0, 4);

            mDataBinding.offerDistance.setText("" + String.format("%s Km(s)", dist));
        } catch (Exception e) {
            e.printStackTrace();
            mDataBinding.offerDistance.setText("Not Found!");
        }
        String currencyCode = topOffer.getCurrencyCode() == null || topOffer.getCurrencyCode().equals("") ? "INR" : topOffer.getCurrencyCode();
        mDataBinding.offerPrice.setText("" + String.format(" %s%d", Common.getCurrencySymbol(currencyCode), topOffer.getOfferPrice()));
        for (int i = 0; i < roomTypeBeanList.size(); i++) {
            if (roomTypeBeanList.get(i).getId().equals(topOffer.getOffered_room_type())) {
                mDataBinding.offerRoom.setText(emptyifnull(roomTypeBeanList.get(i).getName()));
                break;
            }
        }
        return mDataBinding.getRoot();
    }

    private void gotToDetails() {
        Gson gson = new Gson();
        Intent intent = new Intent(getActivity(), BotelDetailsActivity.class);
        intent.putExtra("offer", gson.toJson(topOffer, TopFiveLiveOffer.class));
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewCompat.setElevation(getView(), 10f);
       /* ViewCompat.setElevation(toolbar, 4f);

        toolbar.setTitle(MapTopOfferPagerAdapter.PAGE_TITLES[index]);
        toolbar.inflateMenu(R.menu.fragment);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });*/
    }

    AsyncTaskListener listener = new AsyncTaskListener() {
        @Override
        public void onTaskCancelled(String data) {
            if (progressloadingdialog != null) progressloadingdialog.dismiss();
        }

        @Override
        public void onTaskComplete(String result, String tag) {

            if (result == null) {
                Toast.makeText(getContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
                return;
            }

            response = new Gson().fromJson(result, GooglePlacesResponse.class);
            if (progressloadingdialog != null) progressloadingdialog.dismiss();
            if (response.getStatus().equals("OK")) {
                mDataBinding.hotelName.setText("" + response.getResult().getName());
                mDataBinding.hotelAddress.setText("" + response.getResult().getFormattedAddress());

                String rating = "";

                if (0 < response.getResult().getRating()) {
                    rating = response.getResult().getRating() + "";
                }

                if (TextUtils.isEmpty(rating)) {
                    mDataBinding.ratingGroup.setVisibility(View.INVISIBLE);
                } else {
                    mDataBinding.ratingGroup.setVisibility(View.VISIBLE);
                    mDataBinding.hotelRating.setText("" + rating);
                    try {
                        mDataBinding.upperRatingBar.setRating((int) Math.round(Double.parseDouble(rating)));
                    } catch (NumberFormatException nfe) {
                        mDataBinding.ratingGroup.setVisibility(View.INVISIBLE);
                    }

                }

                String reviews = "";

                if (response.getResult().getReviews() != null && response.getResult().getReviews().size() > 0) {
                    reviews =""+ response.getResult().getUserRatingsTotal();
                }

                if (TextUtils.isEmpty(reviews)) {
                    mDataBinding.hotelReviews.setVisibility(View.INVISIBLE);
                } else {
                    mDataBinding.hotelReviews.setVisibility(View.VISIBLE);
                    mDataBinding.hotelReviews.setText("" + reviews + " Reviews");
                }


            } else {
                //mDataBinding.seeDetails.setEnabled(false);
                mDataBinding.layout.setVisibility(View.GONE);
                Toast.makeText(getContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(),"Error Places: "+response.getStatus(),Toast.LENGTH_SHORT).show();
            }


        }
    };


    @Override
    public void onClick(View v) {
        gotToDetails();
    }
}
