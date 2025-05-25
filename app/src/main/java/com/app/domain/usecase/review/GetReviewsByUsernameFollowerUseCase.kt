package com.app.domain.usecase.review

import com.app.domain.model.PublicReview
import com.app.domain.repository.ReviewRepository

class GetReviewsByUsernameFollowerUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(username: String): List<PublicReview>{
        return reviewRepository.getReviewsByUsernameFollower(username)
    }
}