package com.app.domain.usecase.review

import com.app.domain.model.Review
import com.app.domain.repository.ReviewRepository


class GetReviewsUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(): List<Review>{
        return reviewRepository.getReviews()
    }
}