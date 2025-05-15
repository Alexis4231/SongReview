package com.app.data.retrofit

import com.app.data.api.YoutubeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YoutubeRetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: YoutubeApiService by lazy {
        YoutubeRetrofitClient.retrofit.create(YoutubeApiService::class.java)
    }
}