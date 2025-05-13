package com.app.domain.usecase.review

import com.app.domain.model.Review
import com.app.domain.repository.ReviewRepository

class SaveReviewUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(review: Review): Boolean{
        return reviewRepository.save(review)
    }
}