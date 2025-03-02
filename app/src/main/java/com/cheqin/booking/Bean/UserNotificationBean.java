package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

public class UserNotificationBean {

    @SerializedName("msge")
    private String msge;
    @SerializedName("msgid")
    private String msgid;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getmsge() {
        return msge;
    }

    public void setmsge(String msge) {
        this.msge = msge;
    }

}
