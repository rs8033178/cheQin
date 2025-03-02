package com.cheqin.booking.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suhas on 22-Dec-15.
 */
public class RoomTypeBean {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("rank")
    private String rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

}
