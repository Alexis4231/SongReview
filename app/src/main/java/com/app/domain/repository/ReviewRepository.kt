package com.app.domain.repository

import com.app.domain.model.Review

interface ReviewRepository {
    suspend fun save(review: Review): Boolean
    suspend fun getReviews(): List<Review>
    suspend fun getReviewsByCodeSong(codeSong: String): List<Review>
}