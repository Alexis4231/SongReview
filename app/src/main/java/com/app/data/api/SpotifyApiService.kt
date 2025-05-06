package com.app.data.api

import com.app.domain.model.AccessTokenResponse
import com.app.domain.model.Artist
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyApiService {
    @GET("spotify-token")
    suspend fun getToken(): AccessTokenResponse

    @GET("v1/artists/{id}")
    suspend fun getArtistById(
        @Header("Authorization") authHeader: String,
        @Path("id") artistId: String
    ): Artist
}