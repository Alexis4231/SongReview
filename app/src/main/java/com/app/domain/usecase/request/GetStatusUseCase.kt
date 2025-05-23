package com.app.domain.usecase.request

import com.app.domain.repository.RequestRepository

class GetStatusUseCase(private val requestRepository: RequestRepository) {
    suspend operator fun invoke(username: String): Map<String, Boolean>?{
        return requestRepository.getStatus(username)
    }
}