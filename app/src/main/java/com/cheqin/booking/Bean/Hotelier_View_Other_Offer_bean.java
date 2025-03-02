package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

public class Hotelier_View_Other_Offer_bean {
    @SerializedName("hotelier_offered_price")
    private String hotelier_offered_price = null;
    @SerializedName("hotelier_total_price")
    private String hotelier_total_price = null;
    @SerializedName("hotelier_offered_till")
    private String hotelier_offered_till = null;
    @SerializedName("currency")
    private String currency = null;


    public String getHotelier_offered_price() {
        return hotelier_offered_price;
    }
    public void setHotelier_offered_price(String hotelier_offered_price) {
        this.hotelier_offered_price = hotelier_offered_price;
    }

    public String getHotelier_total_price() {
        return hotelier_total_price;
    }
    public void setHotelier_total_price(String hotelier_total_price) {
        this.hotelier_total_price = hotelier_total_price;
    }

    public String getHotelier_offered_till() {
        return hotelier_offered_till;
    }

    public void setHotelier_offered_till(String hotelier_offered_till) {
        this.hotelier_offered_till = hotelier_offered_till;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
