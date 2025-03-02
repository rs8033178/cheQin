package com.cheqin.booking.network.models.response;

import com.google.gson.annotations.SerializedName;

public class AbstractResponse {

    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("msg")
    private String msg;
    @SerializedName("success")
    private Boolean success;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}