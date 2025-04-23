package com.app.domain.usecase.user

import com.app.domain.model.User
import com.app.domain.repository.UserRepository

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(code: String): User?{
        return userRepository.getByCode(code)
    }
}