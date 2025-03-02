package com.cheqin.booking.Bean;


import com.google.gson.annotations.SerializedName;

public class Confirm_Bean {
    @SerializedName("item_name")
    private String item_name;
    @SerializedName("Hotel_price")
    private String Hotel_price;
    @SerializedName("cid")
    private String cid;
    @SerializedName("date")
    private String date;
    @SerializedName("purchase")
    private String purchase;
    @SerializedName("check_in_date")
    private String check_in_date;
    @SerializedName("check_out_date")
    private String check_out_date;
    @SerializedName("currency")
    private String currency;
    @SerializedName("address")
    private String address;
    @SerializedName("offered_room_type")
    private String offered_room_type;
    @SerializedName("hotel_longitude")
    private double hotel_longitude;
    @SerializedName("hotel_latitude")
    private double hotel_latitude;
    @SerializedName("latitude")
    private double requestLatitude;
    @SerializedName("longitude")
    private double requestLongitude;
    @SerializedName("hotel_name")
    private String hotel_name;
    @SerializedName("hotel_class")
    private String hotel_class;


    public double getRequestLatitude() {
        return requestLatitude;
    }

    public void setRequestLatitude(double requestLatitude) {
        this.requestLatitude = requestLatitude;
    }

    public double getRequestLongitude() {
        return requestLongitude;
    }

    public void setRequestLongitude(double requestLongitude) {
        this.requestLongitude = requestLongitude;
    }

    private String hotelier_mobile;
    private String hotelier_email;
    private String guest_mobile;
    private String guest_email;

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    private String current_status;

    public String getHotelier_mobile() {
        return hotelier_mobile;
    }

    public void setHotelier_mobile(String hotelier_mobile) {
        this.hotelier_mobile = hotelier_mobile;
    }

    public String getHotelier_email() {
        return hotelier_email;
    }

    public void setHotelier_email(String hotelier_email) {
        this.hotelier_email = hotelier_email;
    }

    public String getGuest_mobile() {
        return guest_mobile;
    }

    public void setGuest_mobile(String guest_mobile) {
        this.guest_mobile = guest_mobile;
    }

    public String getGuest_email() {
        return guest_email;
    }

    public void setGuest_email(String guest_email) {
        this.guest_email = guest_email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getHotel_price() {
        return Hotel_price;
    }

    public void setHotel_price(String hotel_price) {
        Hotel_price = hotel_price;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffered_room_type() {
        return offered_room_type;
    }

    public void setOffered_room_type(String offered_room_type) {
        this.offered_room_type = offered_room_type;
    }

    public double getHotel_longitude() {
        return hotel_longitude;
    }

    public void setHotel_longitude(double hotel_longitude) {
        this.hotel_longitude = hotel_longitude;
    }

    public double getHotel_latitude() {
        return hotel_latitude;
    }

    public void setHotel_latitude(double hotel_latitude) {
        this.hotel_latitude = hotel_latitude;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_class() {
        return hotel_class;
    }

    public void setHotel_class(String hotel_class) {
        this.hotel_class = hotel_class;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
