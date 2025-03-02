package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suhas Kumar S on 11-Dec-15.
 */
public class UserExpiredbean {
    @SerializedName("Hotel_type")
    private String Hotel_type = null;
    @SerializedName("Item_info")
    private String Item_info = null;
    @SerializedName("Check_in_date")
    private String Check_in_date = null;
    @SerializedName("Check_out_date")
    private String Check_out_date = null;
    @SerializedName("Adults")
    private String Adults = null;
    @SerializedName("Childrens")
    private String Childrens = null;
    @SerializedName("Rooms")
    private String Rooms = null;
    @SerializedName("Locality")
    private String Locality = null;
    @SerializedName("numb")
    private String numb=null;

    public String getnumb() {
        return numb;
    }

    public void setnumb(String numb) {
        this.numb = numb ;
    }

    private boolean status = false;

    public String getCheck_in_date() {
        return Check_in_date;
    }

    public String getCheck_out_date() {
        return Check_out_date;
    }

    public String getAdults() {
        return Adults;
    }

    public String getChildrens() {
        return Childrens;
    }

    public String getRooms() {
        return Rooms;
    }

    public String getLocality() {
        return Locality;
    }

    public String getItem_info() { return Item_info;}


    public void setItem_info(String item_info) { this.Item_info = item_info; }

    public void setCheck_in_date(String check_in_date) {
        this.Check_in_date = check_in_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.Check_out_date = check_out_date;
    }

    public void setRooms(String rooms) {
        this.Rooms = rooms;
    }

    public void setAdults(String adults) {
        this.Adults = adults;
    }

    public void setChildrens(String childrens) {
        this.Childrens = childrens;
    }

    public void setLocality(String locality) {
        this.Locality = locality;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getHotel_type() {
        return Hotel_type;
    }

    public void setHotel_type(String hotel_type) {
        Hotel_type = hotel_type;
    }
}
