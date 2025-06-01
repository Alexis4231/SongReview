package com.app.domain.usecase.username

import com.app.domain.repository.UsernameRepository

class ExistUsernameUseCase(private val usernameRepository: UsernameRepository) {
    suspend operator fun invoke(name: String):Boolean{
        return usernameRepository.existUsername(name)
    }
}