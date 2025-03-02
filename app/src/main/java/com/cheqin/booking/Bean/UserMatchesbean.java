package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Suhas kumar S on 16-Nov-15.
 */
public class UserMatchesbean {
    @SerializedName("HotelName")
    private String HotelName = null;
    @SerializedName("HotelName")
    private String HotelAddress = null;
    @SerializedName("TotalPrice")
    private String TotalPrice = null;
    @SerializedName("Distance")
    private String Distance = null;
    @SerializedName("date")
    private Date date = null;
    @SerializedName("image")
    private String image = null;
    @SerializedName("id")
    private String id = null;
    @SerializedName("Latitude")
    private String Latitude = null;
    @SerializedName("str_date")
    private String str_date = null;
    @SerializedName("currency")
    private String currency = null;

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Longitude = null;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    private ArrayList<String> img = null;

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getHotelAddress() {
        return HotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        HotelAddress = hotelAddress;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getHotelCity() {
        return HotelCity;
    }

    public void setHotelCity(String hotelCity) {
        HotelCity = hotelCity;
    }

    private String HotelCity = null;

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
