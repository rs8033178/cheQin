package com.cheqin.booking.models;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("cityName")
    String cityName;
    @SerializedName("placeid")
    String placeid;

    public City(String cityName, String placeid) {
        this.cityName = cityName;
        this.placeid = placeid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
}
