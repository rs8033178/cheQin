package com.cheqin.booking.Bean;

public class hotelierPostedDetailsBean {
    private String offer_expired = null;
    private String offer_price;
    private String reject_offer;
    private String min_price;
    private String hotel_type1;
    private String user_req_id;

    public String getuser_req_id1() {
        return user_req_id;
    }

    public void setuser_req_id1(String user_req_id) {
        this.user_req_id = user_req_id;
    }

    public String getHotel_type1() {
        return hotel_type1;
    }

    public void setHotel_type1(String hotel_type1) {
        this.hotel_type1 = hotel_type1;
    }

    public String getoffer_expired() {
        return offer_expired;
    }

    public void setoffer_expired(String offer_expired) {
        this.offer_expired = offer_expired;
    }

    public String getoffer_price() {
        return offer_price;
    }
    public void setoffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getreject_offer() {
        return reject_offer;
    }

    public void setreject_offer(String reject_offer) {
        this.reject_offer = reject_offer;
    }

    public String getmin_price() {
        return min_price;
    }
    public void setmin_price(String min_price) {
        this.min_price = min_price;
    }



}
