package com.app.domain.model

data class AlbumResponse(
    val data: List<Album>
)

data class Album(
    val id: Int,
    val title: String,
    val genres: GenresResponse
)