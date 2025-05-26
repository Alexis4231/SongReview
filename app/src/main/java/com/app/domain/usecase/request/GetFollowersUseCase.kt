package com.app.domain.usecase.request

import com.app.domain.repository.RequestRepository

class GetFollowersUseCase(private val requestRepository: RequestRepository){
    suspend operator fun invoke(): List<String>{
        return requestRepository.getFollowers()
    }
}