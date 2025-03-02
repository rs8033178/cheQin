
package com.cheqin.booking.mappager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewRating {

    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("review_link")
    @Expose
    private Object reviewLink;

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public Object getReviewLink() {
        return reviewLink;
    }

    public void setReviewLink(Object reviewLink) {
        this.reviewLink = reviewLink;
    }

}
