package com.app.domain.model

data class YoutubeSongResponse(
    val items: List<YoutubeItems>
)

data class YoutubeItems(
    val id: YoutubeLink
)

data class YoutubeLink(
    val videoId: String
)