package com.app.domain.usecase.review

import com.app.domain.model.PublicReview
import com.app.domain.repository.ReviewRepository

class GetReviewsByCodeSongUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(codeSong: String): List<PublicReview>{
        return reviewRepository.getReviewsByCodeSong(codeSong)
    }
}