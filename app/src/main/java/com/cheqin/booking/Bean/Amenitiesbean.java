package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Amenitiesbean implements Serializable {

    /**
     *
     */
    @SerializedName("serialVersionUID")
    private static final long serialVersionUID = 1L;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("isSelected")
    private boolean isSelected;
    @SerializedName("image")
    private String image;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
