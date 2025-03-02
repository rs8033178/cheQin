package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

public class UserNavigationItemBean {
@SerializedName("name")
    private String name;
    @SerializedName("imageId")
    private int imageId;

    public UserNavigationItemBean(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

