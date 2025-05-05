package com.app.domain.model

data class Artist(
    val id: String,
    val name: String,
    val followers: Followers
)

data class Followers(
    val total: Int
)