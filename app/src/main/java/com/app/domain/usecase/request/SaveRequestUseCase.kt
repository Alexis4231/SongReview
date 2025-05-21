package com.app.domain.usecase.request

import com.app.domain.model.Request
import com.app.domain.repository.RequestRepository

class SaveRequestUseCase(private val requestRepository: RequestRepository) {
    suspend operator fun invoke(request: Request): Boolean{
        return requestRepository.save(request)
    }
}