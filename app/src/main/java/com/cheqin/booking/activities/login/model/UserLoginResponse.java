package com.cheqin.booking.activities.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("is_user_present")
    @Expose
    private Boolean isUserPresent;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;

    @SerializedName("OTP")
    @Expose
    private String OTP;

    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("countryNameCode")
    @Expose
    private String  countryNameCode;

    public String getCountryNameCode() {
        return countryNameCode;
    }

    public void setCountryNameCode(String countryNameCode) {
        this.countryNameCode = countryNameCode;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getUserPresent() {
        return isUserPresent;
    }

    public void setUserPresent(Boolean userPresent) {
        isUserPresent = userPresent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }



}
