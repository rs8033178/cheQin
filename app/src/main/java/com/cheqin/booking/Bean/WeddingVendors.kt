package com.cheqin.booking.Bean

import com.google.gson.annotations.SerializedName

data class WeddingService(
    @SerializedName("id") val id: Long, @SerializedName("name") val name: String?, @SerializedName(
        "img_url"
    ) val img_url: String?
)

data class WeddingVenue(
    @SerializedName("id") val id: Long, @SerializedName("name") val name: String?, @SerializedName(
        "parent_id"
    ) val parent_id: Long, @SerializedName("min_qty") val min_qty: Int
)