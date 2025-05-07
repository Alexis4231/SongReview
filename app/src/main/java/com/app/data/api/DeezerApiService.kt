package com.app.data.api

import com.app.domain.model.GenresResponse
import retrofit2.http.GET

interface DeezerApiService {
    @GET("genre")
    suspend fun getGenres(): GenresResponse
}