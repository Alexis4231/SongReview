package com.app.domain.model

data class GenresResponse(
    val data: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
)

