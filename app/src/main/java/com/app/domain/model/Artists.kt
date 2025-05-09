package com.app.domain.model

data class ArtistResponse(
    val data: List<Artist>,
)

data class Artist(
    val id: Int,
    val name: String
)
