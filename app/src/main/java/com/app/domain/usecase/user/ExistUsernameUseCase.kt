package com.app.domain.usecase.user

import com.app.domain.repository.UserRepository

class ExistUsernameUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String):Boolean{
        return userRepository.existUsername(name)
    }
}