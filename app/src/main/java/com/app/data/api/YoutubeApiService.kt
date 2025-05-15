package com.app.data.api

import com.app.domain.model.YoutubeSongResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("v3/search")
    suspend fun searchSong(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("key") key: String = "AIzaSyAUTNlAZbqnFWbZDecGa5_tChXAl-nJ9nk",
    ): YoutubeSongResponse
}