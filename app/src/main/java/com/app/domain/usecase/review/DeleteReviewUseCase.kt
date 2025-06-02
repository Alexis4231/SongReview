package com.app.domain.usecase.review

import com.app.domain.model.PublicReview
import com.app.domain.repository.ReviewRepository

class DeleteReviewUseCase(private val reviewRepository: ReviewRepository){
    suspend operator fun invoke(publicReview: PublicReview): Boolean{
        return reviewRepository.deleteReview(publicReview)
    }
}