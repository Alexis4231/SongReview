package com.app.domain.usecase.user

import com.app.domain.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): List<String>{
        return userRepository.getUsers()
    }
}