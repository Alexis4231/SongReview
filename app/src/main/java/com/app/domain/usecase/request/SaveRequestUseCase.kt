package com.app.domain.usecase.request

import com.app.domain.repository.RequestRepository

class SaveRequestUseCase(private val requestRepository: RequestRepository) {
    suspend operator fun invoke(usernameReceiver: String): Boolean{
        return requestRepository.save(usernameReceiver)
    }
}