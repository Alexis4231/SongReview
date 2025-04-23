package com.app.domain.usecase.user

import com.app.domain.repository.UserRepository

class GetEmailByNameUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String): String{
        return userRepository.getEmailByName(name)
    }
}