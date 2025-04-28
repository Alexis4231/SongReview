package com.app.data.remote

import com.app.domain.model.ArtistResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyApiService {
     @GET("v1/search")
     suspend fun searchArtists(
         @Header("Authorization") token: String,
         @Query("q") query: String,
         @Query("type") type: String = "artist",
         @Query("limit") limit: Int = 50
     ): ArtistResponse
}