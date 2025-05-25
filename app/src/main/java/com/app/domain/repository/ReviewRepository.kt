package com.app.domain.repository

import com.app.domain.model.PublicReview
import com.app.domain.model.Review
import java.util.Date

interface ReviewRepository {
    suspend fun save(review: Review): Boolean
    suspend fun getReviews(): List<PublicReview>
    suspend fun getReviewsByCodeSong(codeSong: String): List<PublicReview>
    suspend fun getReviewsByCodeUser(codeUser: String): List<Review>
    suspend fun getReviewsByUsernameFollower(username: String): List<PublicReview>
}