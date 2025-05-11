package com.app.data.api

import com.app.domain.model.Album
import com.app.domain.model.AlbumResponse
import com.app.domain.model.ArtistResponse
import com.app.domain.model.GenresResponse
import com.app.domain.model.SongResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("artist/{idArtist}/albums")
    suspend fun searchAlbum(
        @Path("idArtist") idArtist: Int,
        @Query("limit") limit: Int = 1
    ): AlbumResponse

    @GET("album/{idAlbum}")
    suspend fun searchAlbumDetails(
        @Path("idAlbum") idAlbum: Int
    ): Album
}