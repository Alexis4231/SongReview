package com.app.domain.model

data class SpotifySongResponse(
    val tracks: Tracks
)

data class Tracks(
    val items: List<Items>
)

data class Items(
    val external_urls: SpotifyLink
)

data class SpotifyLink(
    val spotify: String
)

