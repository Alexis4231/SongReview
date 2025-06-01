package com.app.domain.usecase.username

import com.app.domain.repository.UsernameRepository

class GetUsernamesUseCase(private val usernamesRepository: UsernameRepository) {
    suspend operator fun invoke(): List<String>{
        return usernamesRepository.getUsernames()
    }
}