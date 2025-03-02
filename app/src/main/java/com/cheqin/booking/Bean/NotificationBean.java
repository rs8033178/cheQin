package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VaibhaV on 08-Jan-16.
 */
public class NotificationBean {

    @SerializedName("title")
    private String title="";
    @SerializedName("imagepath")
    private String imagepath="";

    public NotificationBean() {
        super();
    }

    public NotificationBean(String title, String imagepath) {
        super();
        this.title = title;
        this.imagepath = imagepath;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
       // result = prime * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "NotificationBean [ title=" + title + ", imagepath="
                + imagepath +"]";
    }
}
