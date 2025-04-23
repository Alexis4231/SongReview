package com.app.domain.usecase.user

import com.app.domain.repository.UserRepository

class DeleteUserUseCase(private val userRepository: UserRepository){
    suspend operator fun invoke(code: String): Boolean{
        return userRepository.delete(code)
    }
}