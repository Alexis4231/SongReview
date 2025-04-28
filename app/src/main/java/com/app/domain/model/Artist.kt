package com.app.domain.model

import com.google.firebase.firestore.DocumentId

data class Artist(
    @DocumentId
    val code: String = "",
    val title: String,
    val name: String,
    val genre: String,
    val album: String,
    val anio: Int
)

data class Artists(
    val items: List<Artist>
)

data class ArtistResponse(
    val artists: Artists
)