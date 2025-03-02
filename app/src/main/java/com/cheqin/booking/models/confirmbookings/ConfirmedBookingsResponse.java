
package com.cheqin.booking.models.confirmbookings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfirmedBookingsResponse {

    @SerializedName("confirm_count")
    @Expose
    private Integer confirmCount;
    @SerializedName("cancel_count")
    @Expose
    private Integer cancelCount;
    @SerializedName("confirmed_bookings")
    @Expose
    private List<ConfirmedBooking> confirmedBookings = null;
    @SerializedName("canceled_bookings")
    @Expose
    private List<CanceledBooking> canceledBookings = null;

    public Integer getConfirmCount() {
        return confirmCount;
    }

    public void setConfirmCount(Integer confirmCount) {
        this.confirmCount = confirmCount;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public List<ConfirmedBooking> getConfirmedBookings() {
        return confirmedBookings;
    }

    public void setConfirmedBookings(List<ConfirmedBooking> confirmedBookings) {
        this.confirmedBookings = confirmedBookings;
    }

    public List<CanceledBooking> getCanceledBookings() {
        return canceledBookings;
    }

    public void setCanceledBookings(List<CanceledBooking> canceledBookings) {
        this.canceledBookings = canceledBookings;
    }

}
