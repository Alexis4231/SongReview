package com.app.domain.model

data class SongResponse(
    val data: List<Song>
)

data class Song(
    val title: String,
    val artist: Artist,
    val link: String,
    val preview: String
)