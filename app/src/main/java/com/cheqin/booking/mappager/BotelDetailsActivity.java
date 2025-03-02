package com.cheqin.booking.mappager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.cheqin.booking.R;
import com.cheqin.booking.User.PreparePayOnline;
import com.cheqin.booking.databinding.ActivityBotelDetailsBinding;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.mappager.model.TopFiveLiveOffer;
import com.cheqin.booking.modelplaces.GooglePlacesResponse;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;

import java.util.HashMap;

public class BotelDetailsActivity extends AppCompatActivity {

    private TopFiveLiveOffer offer;
    ActivityBotelDetailsBinding mDataBinding;
    private Progressloadingdialog progressloadingdialog;
    String browserKey;
    Button confirmBtn;
    private Button btnConfirmTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_botel_details);
        setSupportActionBar(mDataBinding.toolbar);
        getSupportActionBar().setTitle(null);
        SharedPreference sharedPreference = new SharedPreference();
        browserKey = sharedPreference.getApiKey(getApplicationContext());
        confirmBtn = (Button) findViewById(R.id.btnConfirm);
        btnConfirmTop = findViewById(R.id.btnConfirmTop);
        mDataBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        progressloadingdialog = new Progressloadingdialog(this, "Hold on....");

        if (getIntent() != null) {
            offer = new Gson().fromJson(getIntent().getStringExtra("offer"), TopFiveLiveOffer.class);
            //mDataBinding.setTopFiveLiveOffer(offer);
        }

        if (offer == null) {
            finish();
        }

        String url = "https://maps.googleapis.com/maps/api/place/details/json?";

        HashMap<String, String> params = new HashMap<>();
        params.put("key", browserKey);
        params.put("placeid", offer.getG_place_id());
        params.put("fields", "review,photo,name,rating,formatted_address,formatted_phone_number,user_ratings_total");


        HttpAsync httpAsync = new HttpAsync(this, listener, url, params, 1, "googleapi");
        progressloadingdialog.show();
        httpAsync.execute();

        confirmBtn.setText("Confirm " + Common.getCurrencySymbol("INR") + offer.getOfferPrice());
        btnConfirmTop.setText("Confirm " + Common.getCurrencySymbol("INR") + offer.getOfferPrice());

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayActivity();
            }
        });

        btnConfirmTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayActivity();
            }
        });
    }

    private void startPayActivity() {
        Intent i = new Intent(getApplicationContext(), PreparePayOnline.class);
        i.putExtra("offer", new Gson().toJson(offer, TopFiveLiveOffer.class));
        startActivity(i);
    }

    AsyncTaskListener listener = new AsyncTaskListener() {
        @Override
        public void onTaskCancelled(String data) {
            if (progressloadingdialog != null) progressloadingdialog.dismiss();
        }

        @Override
        public void onTaskComplete(String result, String tag) {
            try {
                if (result == null) {
                    return;
                }
                GooglePlacesResponse response = new Gson().fromJson(result, GooglePlacesResponse.class);
                if (progressloadingdialog != null) progressloadingdialog.dismiss();
                if (response.getStatus().equals("OK")) {
                    mDataBinding.content.btnConfirm.setVisibility(View.VISIBLE);


                    mDataBinding.setResult(response.getResult());
                    if (response.getResult() != null) {
                        if (response.getResult().getPhotos() != null) {
                            SlideShowPagerAdapter mSlideShowAdapter =
                                    new SlideShowPagerAdapter(BotelDetailsActivity.this, response.getResult().getPhotos());
                            mDataBinding.imageSlideshowPager.setAdapter(mSlideShowAdapter);
                        }
                        mDataBinding.imageSlideshowPager.setAutoScrollDurationFactor(10);
                        mDataBinding.imageSlideshowPager.setInterval(3000);
                        mDataBinding.imageSlideshowPager.startAutoScroll();
                        // Log.v("BotelDetailsActivity","rating-"+response.getResult().getRating());
                        mDataBinding.content.totalReviewsValue.setText("" + response.getResult().getRating());
                        mDataBinding.content.hotelRatings.setRating((float) response.getResult().getRating());

                        int userRatingTotal = response.getResult().getUserRatingsTotal();
                        if (userRatingTotal > 0) {
                            mDataBinding.content.totalReviews.setText(String.format("(%d)", userRatingTotal));
                        }
                        if (response.getResult().getReviews() != null && response.getResult().getReviews().size() > 0) {
                            ReviewsAdapter reviewAdapter = new ReviewsAdapter(response.getResult().getReviews(), BotelDetailsActivity.this);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            mDataBinding.content.reviewsRecyclerView.setLayoutManager(mLayoutManager);
                            mDataBinding.content.reviewsRecyclerView.setAdapter(reviewAdapter);

                            reviewAdapter.notifyDataSetChanged();
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
