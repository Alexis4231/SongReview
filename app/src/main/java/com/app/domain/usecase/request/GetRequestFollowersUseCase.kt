package com.app.domain.usecase.request

import com.app.domain.repository.RequestRepository

class GetRequestFollowersUseCase(private val requestRepository: RequestRepository){
    suspend operator fun invoke(): List<String>{
        return requestRepository.getRequestFollowers()
    }
}