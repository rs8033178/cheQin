package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 12/13/2015.
 */
public class HotelierOfferDetailsBean {
    @SerializedName("hotel_info")
    private String hotel_info;
    @SerializedName("user_price")
    private String user_price;
    @SerializedName("locality")
    private String locality;
    @SerializedName("my_offer_rank")
    private String my_offer_rank;
    @SerializedName("category_name")
    private String category_name;
    @SerializedName("user_request_id")
    private String user_request_id;
    @SerializedName("moid")
    private String moid;
    @SerializedName("Your_price")
    private String Your_price = null;
    @SerializedName("min_price")
    private String min_price = null;
    @SerializedName("min_time")
    private String min_time = null;
    @SerializedName("lengt")
    private String lengt = null;
    @SerializedName("currency")
    private String currency = null;
    @SerializedName("check_in_date")
    private String check_in_date = null;
    @SerializedName("check_out_date")
    private String check_out_date = null;
    @SerializedName("rooms")
    private String rooms = null;
    @SerializedName("adults")
    private String adults = null;
    @SerializedName("childrens")
    private String childrens = null;
    @SerializedName("lowest_offer")
    private String lowest_offer = null;


    public String getLengt() {
        return lengt;
    }

    public void setLengt(String lengt) {
        this.lengt = lengt;
    }

    public String getMin_price() {
        return min_price;
    }


    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMin_time() {
        return min_time;
    }

    public void setMin_time(String min_time) {
        this.min_time = min_time;
    }

    public String getYour_price() {
        return Your_price;
    }

    public void setYour_price(String your_price) {
        Your_price = your_price;
    }

    private String Offer_price;

    public String getUser_request_id() {
        return user_request_id;
    }

    public void setUser_request_id(String user_request_id) {
        this.user_request_id = user_request_id;
    }

    public String getMoid() {
        return moid;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    public String getHotel_info() {
        return hotel_info;
    }

    public void setHotel_info(String hotel_info) {
        this.hotel_info = hotel_info;
    }

    public String getUser_price() {
        return user_price;
    }

    public void setUser_price(String user_price) {
        this.user_price = user_price;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMy_offer_rank() {
        return my_offer_rank;
    }

    public void setMy_offer_rank(String my_offer_rank) {
        this.my_offer_rank = my_offer_rank;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getOffer_price() {
        return Offer_price;
    }

    public void setOffer_price(String offer_price) {
        Offer_price = offer_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getChildrens() {
        return childrens;
    }

    public void setChildrens(String childrens) {
        this.childrens = childrens;
    }

    public String getLowest_offer() {
        return lowest_offer;
    }

    public void setLowest_offer(String lowest_offer) {
        this.lowest_offer = lowest_offer;
    }
}
