package com.app.domain.usecase.request

import com.app.domain.repository.RequestRepository

class DeleteRequestUseCase(private val requestRepository: RequestRepository) {
    suspend operator fun invoke(username: String): Boolean{
        return requestRepository.deleteRequest(username)
    }
}