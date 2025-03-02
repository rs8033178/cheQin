package com.cheqin.booking.Bean

import com.google.gson.annotations.SerializedName

data class ConfirmationBean(@SerializedName("adults") var adults:String ,
                            @SerializedName(  "rooms") var rooms:String ,
                            @SerializedName("nights") var nights:String ,
                            @SerializedName("selectedRoom") var selectedRoom:String,
                            @SerializedName("hotel_class")val hotelClass: CharSequence?) {

}