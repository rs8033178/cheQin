package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suhas kumar S on 18-Oct-15.
 */
public class Userbean {
    @SerializedName("grand_total")
    private double grandTotal;
    @SerializedName("item_info")
    private String Hotel_type = null;
    private String Item_info = null;
    @SerializedName("check_in_date")
    private String Check_in_date = null;
    @SerializedName("check_out_date")
    private String Check_out_date = null;
    @SerializedName(("check_in_date1"))
    private String check_in_date1 = null;
    @SerializedName(("check_in_date2"))
    private String check_in_date2 = null;
    @SerializedName("adult")
    private String Adults = null;
    @SerializedName("children")
    private String Childrens = null;
    @SerializedName("no_of_rooms")
    private String Rooms = null;
    @SerializedName("locality")
    private String Locality = null;
    @SerializedName("locality")
    private String numb = null;
    private String offered = null;
    private String price = null;
    @SerializedName("is_short_term")
    private String short_stay = null;
    @SerializedName("currency_code")
    private String currency = null;
    private boolean isActionEnabled;
    private boolean isExpired;

    @SerializedName("is_wedding_exhibition")
    private boolean isExhibition;
    @SerializedName("category_name")
    private String exhibitionCategoryName;

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public String getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(String room_type_id) {
        this.room_type_id = room_type_id;
    }

    private String room_type_id = null;


    public String getShort_stay() {
        return short_stay;
    }

    public void setShort_stay(String short_stay) {
        this.short_stay = short_stay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOffered() {
        return offered;
    }

    public void setOffered(String offered) {
        this.offered = offered;
    }

    public String getnumb() {
        return numb;
    }

    public void setnumb(String numb) {
        this.numb = numb;
    }

    private boolean status = false;

    public String getHotel_type() {
        return Hotel_type;
    }

    public void setHotel_type(String hotel_type) {
        Hotel_type = hotel_type;
    }

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

    public String getItem_info() {
        return Item_info;
    }


    public void setItem_info(String item_info) {
        this.Item_info = item_info;
    }

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setActionEnabled(boolean isEnabled) {
        this.isActionEnabled = isEnabled;
    }

    public boolean isActionEnabled() {
        return isActionEnabled;
    }

    public void setExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public String getCheck_in_date1() {
        return check_in_date1;
    }

    public String getCheck_in_date2() {
        return check_in_date2;
    }

    public boolean isExhibition() {
        return isExhibition;
    }

    public void setCheck_in_date1(String check_in_date1) {
        this.check_in_date1 = check_in_date1;
    }

    public void setCheck_in_date2(String check_in_date2) {
        this.check_in_date2 = check_in_date2;
    }

    public void setExhibition(boolean exhibition) {
        isExhibition = exhibition;
    }

    public void setExhibitionCategoryName(String exhibitionCategoryName) {
        this.exhibitionCategoryName = exhibitionCategoryName;
    }

    public String getExhibitionCategoryName() {
        return exhibitionCategoryName;
    }
}
//    public int setItem_info() {
//        return 0;
//    }

