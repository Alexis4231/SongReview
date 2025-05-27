package com.app.domain.usecase.user

import com.app.domain.repository.UserRepository

class UpdateFcmTokenUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Boolean{
        return userRepository.updateFcmToken()
    }
}