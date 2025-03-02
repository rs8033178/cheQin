
package com.cheqin.booking.mappager.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopOffersResponse {

    @SerializedName("request_detail")
    @Expose
    private RequestDetail requestDetail;
    @SerializedName("requested_hotel_amenities")
    @Expose
    private List<RequestedHotelAmenity> requestedHotelAmenities = null;
    @SerializedName("available_hotel_amenities")
    @Expose
    private List<AvailableHotelAmenity> availableHotelAmenities = null;
    @SerializedName("possible_offer_expiry_hrs")
    @Expose
    private List<Integer> possibleOfferExpiryHrs = null;
    @SerializedName("top_five_live_offers")
    @Expose
    private List<TopFiveLiveOffer> topFiveLiveOffers = null;
    @SerializedName("top_five_expired_offers")
    @Expose
    private List<Object> topFiveExpiredOffers = null;
    @SerializedName("my_offer")
    @Expose
    private Object myOffer;
    @SerializedName("mydeal_fee_percent")
    @Expose
    private String mydealFeePercent;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("is_travel_agent")
    @Expose
    private Integer isTravelAgent;
    @SerializedName("req_total_days")
    @Expose
    private Integer reqTotalDays;
    @SerializedName("room_type_id")
    @Expose
    private String offered_room_type;


    public String getOffered_room_type() {
        return offered_room_type;
    }

    public void setOffered_room_type(String offered_room_type) {
        this.offered_room_type = offered_room_type;
    }

    public RequestDetail getRequestDetail() {
        return requestDetail;
    }

    public void setRequestDetail(RequestDetail requestDetail) {
        this.requestDetail = requestDetail;
    }

    public List<RequestedHotelAmenity> getRequestedHotelAmenities() {
        return requestedHotelAmenities;
    }

    public void setRequestedHotelAmenities(List<RequestedHotelAmenity> requestedHotelAmenities) {
        this.requestedHotelAmenities = requestedHotelAmenities;
    }

    public List<AvailableHotelAmenity> getAvailableHotelAmenities() {
        return availableHotelAmenities;
    }

    public void setAvailableHotelAmenities(List<AvailableHotelAmenity> availableHotelAmenities) {
        this.availableHotelAmenities = availableHotelAmenities;
    }

    public List<Integer> getPossibleOfferExpiryHrs() {
        return possibleOfferExpiryHrs;
    }

    public void setPossibleOfferExpiryHrs(List<Integer> possibleOfferExpiryHrs) {
        this.possibleOfferExpiryHrs = possibleOfferExpiryHrs;
    }

    public List<TopFiveLiveOffer> getTopFiveLiveOffers() {
        return topFiveLiveOffers;
    }

    public void setTopFiveLiveOffers(List<TopFiveLiveOffer> topFiveLiveOffers) {
        this.topFiveLiveOffers = topFiveLiveOffers;
    }

    public List<Object> getTopFiveExpiredOffers() {
        return topFiveExpiredOffers;
    }

    public void setTopFiveExpiredOffers(List<Object> topFiveExpiredOffers) {
        this.topFiveExpiredOffers = topFiveExpiredOffers;
    }

    public Object getMyOffer() {
        return myOffer;
    }

    public void setMyOffer(Object myOffer) {
        this.myOffer = myOffer;
    }

    public String getMydealFeePercent() {
        return mydealFeePercent;
    }

    public void setMydealFeePercent(String mydealFeePercent) {
        this.mydealFeePercent = mydealFeePercent;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getIsTravelAgent() {
        return isTravelAgent;
    }

    public void setIsTravelAgent(Integer isTravelAgent) {
        this.isTravelAgent = isTravelAgent;
    }

    public Integer getReqTotalDays() {
        return reqTotalDays;
    }

    public void setReqTotalDays(Integer reqTotalDays) {
        this.reqTotalDays = reqTotalDays;
    }

}
