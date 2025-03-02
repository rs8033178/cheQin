package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

public class hotelier_show_requests_bean {
    @SerializedName("checkin")
    private String checkin;
    @SerializedName("checkout")
    private String checkout;
    @SerializedName("price")
    private String price;
    @SerializedName("no_of_rooms")
    private String no_of_rooms;
    @SerializedName("adult")
    private String adult;
    @SerializedName("cildren")
    private String cildren;
    @SerializedName("item_info")
    private String item_info;
    @SerializedName("city")
    private String city;
    @SerializedName("id")
    private String id;
    @SerializedName("user_distance")
    private String user_distance;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("currency")
    private String currency;

    public String getTop_offer() {
        return top_offer;
    }

    public void setTop_offer(String top_offer) {
        this.top_offer = top_offer;
    }

    private String short_stay;
    private String top_offer;

    public String getShort_stay() {
        return short_stay;
    }

    public void setShort_stay(String short_stay) {
        this.short_stay = short_stay;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private boolean status = false;
//    private String amenities;

    public void setprice(String price) {
        this.price = price;
    }



    public String getprice(){
        return price;
    }

    public String getno_of_rooms() {
        return no_of_rooms;
    }

    public void setno_of_rooms(String no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }

    public String getadult() {
        return adult;
    }

    public void setadult(String adult) {
        this.adult = adult;
    }

    public String getitem_info() {
        return item_info;
    }

    public void setitem_info(String item_info) {
        this.item_info = item_info;
    }

    public String getcity() {
        return city;
    }

    public void setcity(String city) {
        this.city = city;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getCildren() {
        return cildren;
    }

    public void setCildren(String cildren) {
        this.cildren = cildren;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUser_distance() {
        return user_distance;
    }

    public void setUser_distance(String user_distance) {
        this.user_distance = user_distance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
