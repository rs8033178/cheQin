package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

public class bean_faq {
    @SerializedName("que")
    private  String que = null;
    @SerializedName("ans")
    private String ans = null;

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }



}
