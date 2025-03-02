package com.cheqin.booking.gcm

import UtelNotificationModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationRemotesService {

    @GET("/my_deals/user_push_notifications/get.json?device_type=ANDROID")
    suspend fun fetchNotifications(@Query("auth_token") authToken: String): UtelNotificationModel

    @GET("/my_deals/mark_all_push_notification_read/get.json?device_type=ANDROID")
    suspend fun markAllNotificationRead(@Query("auth_token") authToken: String)

}