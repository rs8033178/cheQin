package com.cheqin.booking.User

import CitySuggestion
import Predictions
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface PlaceSuggestionService {

    @GET("maps/api/place/autocomplete/json")
    fun getCitySuggestion(
        @Query("input") input: String,
        @Query("types") types: String,
        @Query("key") browserKey: String
    ): Call<CitySuggestion>


    @GET("maps/api/place/autocomplete/json")
    fun getAreaSuggestion(
        @Query("input") input: String,
        @Query("radius") radius: String,
        @Query("sensor") sensor: String,
        @Query("location") location: String,
        @Query("key") browserKey: String
    ): Call<CitySuggestion>
}

class PlacesRemoteRepo {

    private val retrofit: Retrofit
    private val placeSuggestionService: PlaceSuggestionService

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        placeSuggestionService = retrofit.create(PlaceSuggestionService::class.java)
    }

    companion object {

        private var INSTANCE: PlacesRemoteRepo? = null

        @JvmStatic
        fun getInstance(): PlacesRemoteRepo = synchronized(this) {
            INSTANCE ?: PlacesRemoteRepo().also {
                INSTANCE = it
            }
        }
    }

    fun getAreaSuggestions(
        query: String,
        location: String,
        browserKey: String,
        radius: String = "100",
        sensor: String = "true"
    ): List<Predictions>? {

        val response = placeSuggestionService.getAreaSuggestion(
            input = query,
            location = location,
            browserKey = browserKey,
            radius = radius,
            sensor = sensor
        )
        val body = response.execute().body()
        return body?.predictions

    }

    fun getCitySuggestion(
        query: String,
        types: String,
        browserKey: String
    ): List<Predictions>? {
        val response =
            placeSuggestionService.getCitySuggestion(query, types, browserKey)
        val body = response.execute().body()
        return body?.predictions
    }

}