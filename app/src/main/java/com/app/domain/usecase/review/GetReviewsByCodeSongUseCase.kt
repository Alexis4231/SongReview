package com.app.domain.usecase.review

import com.app.domain.model.Review
import com.app.domain.repository.ReviewRepository

class GetReviewsByCodeSongUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(codeSong: String): List<Review>{
        return reviewRepository.getReviewsByCodeSong(codeSong)
    }
}