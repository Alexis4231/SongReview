package com.app.domain.usecase.user

import com.app.domain.model.User
import com.app.domain.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): List<User>{
        return userRepository.getUsers()
    }

}