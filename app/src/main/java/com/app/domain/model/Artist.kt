package com.app.domain.model

data class ArtistResponse(
    val data: List<ArtistData>
)

data class ArtistData(
    val id: Int,
    val name: String,
    val link: String
)