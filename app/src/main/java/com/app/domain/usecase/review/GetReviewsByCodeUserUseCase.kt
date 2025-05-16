package com.app.domain.usecase.review

import com.app.domain.model.Review
import com.app.domain.repository.ReviewRepository

class GetReviewsByCodeUserUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(codeUser: String): List<Review>{
        return reviewRepository.getReviewsByCodeUser(codeUser)
    }
}