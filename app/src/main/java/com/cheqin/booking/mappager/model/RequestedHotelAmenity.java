
package com.cheqin.booking.mappager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestedHotelAmenity {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_request_id")
    @Expose
    private Integer userRequestId;
    @SerializedName("amenity_id")
    @Expose
    private Integer amenityId;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserRequestId() {
        return userRequestId;
    }

    public void setUserRequestId(Integer userRequestId) {
        this.userRequestId = userRequestId;
    }

    public Integer getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Integer amenityId) {
        this.amenityId = amenityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
