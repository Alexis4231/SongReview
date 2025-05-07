package com.app.data.api

import com.app.domain.model.ArtistResponse
import com.app.domain.model.GenresResponse
import com.app.domain.model.SongResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerApiService {
    @GET("genre")
    suspend fun getGenres(): GenresResponse

    @GET("search/artist")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("limit") limit: Int = 50,
        @Query("index") page: Int = 0
    ): ArtistResponse

    @GET("search")
    suspend fun searchSongs(
        @Query("q") query: String,
        @Query("limit") limit: Int = 50,
        @Query("index") page: Int = 0
    ): SongResponse

}