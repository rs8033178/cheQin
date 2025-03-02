package com.cheqin.booking.mappager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cheqin.booking.R;
import com.cheqin.booking.modelplaces.Review;

import java.util.List;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {
    private List<Review> mDataset;

    public ReviewsAdapter(List<Review> mDataset, Context mContext) {
        this.mDataset = mDataset;
        this.mContext = mContext;
    }

    private Context mContext;


    public static class ReviewsHolder extends RecyclerView.ViewHolder {

        AppCompatTextView reviewerName;
        AppCompatTextView review;
        CircleImageView reviewerPic;
        AppCompatRatingBar rating;

        public ReviewsHolder(View itemView) {
            super(itemView);
            this.reviewerName = itemView.findViewById(R.id.reviewer_name);
            this.review = itemView.findViewById(R.id.review);
            this.reviewerPic = itemView.findViewById(R.id.reviewer_pic);
            this.rating = itemView.findViewById(R.id.user_ratings);
        }
    }


    @Override
    public ReviewsAdapter.ReviewsHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review_item, parent, false);
        ReviewsHolder vh = new ReviewsHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReviewsHolder holder, int position) {
        holder.reviewerName.setText(mDataset.get(position).getAuthorName());
        holder.review.setText(mDataset.get(position).getText());
        holder.rating.setRating(mDataset.get(position).getRating());
        Glide.with(mContext)
                .load(mDataset.get(position).getProfilePhotoUrl())
                //.placeholder(R.drawable.image_placeholder)
                //.fitCenter()
                .into(holder.reviewerPic);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
