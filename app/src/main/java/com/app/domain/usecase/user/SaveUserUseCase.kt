package com.app.domain.usecase.user

import com.app.domain.model.User
import com.app.domain.repository.UserRepository

class SaveUserUseCase(private val userRepository: UserRepository) {
   suspend operator fun invoke(user: User): Boolean{
        return userRepository.save(user)
   }
}