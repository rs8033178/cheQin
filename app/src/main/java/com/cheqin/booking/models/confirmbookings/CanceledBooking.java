
package com.cheqin.booking.models.confirmbookings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CanceledBooking {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("purchase_code")
    @Expose
    private String purchaseCode;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("purchased_by")
    @Expose
    private String purchasedBy;
    @SerializedName("mode_of_shipping")
    @Expose
    private Object modeOfShipping;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("request_detail")
    @Expose
    private RequestDetail requestDetail;
    @SerializedName("offer_detail")
    @Expose
    private OfferDetail offerDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public Object getModeOfShipping() {
        return modeOfShipping;
    }

    public void setModeOfShipping(Object modeOfShipping) {
        this.modeOfShipping = modeOfShipping;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public RequestDetail getRequestDetail() {
        return requestDetail;
    }

    public void setRequestDetail(RequestDetail requestDetail) {
        this.requestDetail = requestDetail;
    }

    public OfferDetail getOfferDetail() {
        return offerDetail;
    }

    public void setOfferDetail(OfferDetail offerDetail) {
        this.offerDetail = offerDetail;
    }

}
