package com.app.data.retrofit

import com.app.data.api.SpotifyApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyRetrofitAuth{
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://spotifygettoken.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SpotifyApiService by lazy {
        retrofit.create(SpotifyApiService::class.java)
    }
}