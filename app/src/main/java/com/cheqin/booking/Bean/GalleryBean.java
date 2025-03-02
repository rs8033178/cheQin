package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 12-02-2016.
 */
public class GalleryBean implements Serializable {
    @SerializedName("img")
    private String img;
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
