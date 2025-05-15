package com.app.data.api

import com.app.domain.model.AccessTokenResponse
import com.app.domain.model.Artist
import com.app.domain.model.SpotifySongResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApiService {
    @GET("spotify-token")
    suspend fun getToken(): AccessTokenResponse

    @GET("v1/search")
    suspend fun searchSong(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 1
    ): SpotifySongResponse
}