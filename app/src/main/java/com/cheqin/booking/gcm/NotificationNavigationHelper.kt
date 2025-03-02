package com.cheqin.booking.gcm

import android.content.Context
import android.content.Intent
import com.cheqin.booking.Adapter.UserCancelBookingAdapter
import com.cheqin.booking.User.UserBooking
import com.cheqin.booking.User.UserCancelRequest
import com.cheqin.booking.User.UserConfirmBooking
import com.cheqin.booking.User.UserLiveRequests
import com.cheqin.booking.models.confirmbookings.ConfirmedBooking

const val USR_LOWEST_OFFER = "USR_LOWEST_OFFER"
const val USR_BOOKING_CONFIRMATION = "USR_BOOKING_CONFIRMATION"
const val USR_BOOKING_CANCEL = "USR_BOOKING_CANCEL"
const val HOME = "HOME"

const val OBJECT_ID = "objectId"

fun getNavigationIntent(context: Context, type: String?, objectId: Long?): Intent? {

    when (type) {
        USR_LOWEST_OFFER -> {

            return Intent(context, UserLiveRequests::class.java).apply {
                putExtra(OBJECT_ID, objectId)
            }

        }
        USR_BOOKING_CONFIRMATION -> {

            return Intent(context, UserConfirmBooking::class.java).apply {
                putExtra(OBJECT_ID, objectId)
            }

        }
        USR_BOOKING_CANCEL -> {

            return Intent(context, UserCancelRequest::class.java).apply {
                putExtra(OBJECT_ID, objectId)
            }

        }
        else -> {
            return null
        }
    }

}