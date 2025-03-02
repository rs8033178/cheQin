package com.cheqin.booking.gcm

import UtelNotificationItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NotificationRemoteDataSource {

    private const val BASE_URL = "http://api.mypillows.company/";

    private val retrofit: Retrofit
    private val notificationService: NotificationRemotesService

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        notificationService = retrofit.create(NotificationRemotesService::class.java)
    }

    suspend fun fetchNotifications(authToken: String): List<UtelNotificationItem> {
        val notificationModel = notificationService.fetchNotifications(authToken)

        val notificationList = mutableListOf<UtelNotificationItem>()

        if (!notificationModel.unread_messages.isNullOrEmpty())
            notificationList.addAll(notificationModel.unread_messages)

        if (!notificationModel.read_messages.isNullOrEmpty())
            notificationList.addAll(notificationModel.read_messages)

        return notificationList.toList()

    }

    suspend fun markAllNotificationRead(authToken: String) {
        notificationService.markAllNotificationRead(authToken)
    }

}