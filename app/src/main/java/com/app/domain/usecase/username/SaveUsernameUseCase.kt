package com.app.domain.usecase.username

import com.app.domain.model.Username
import com.app.domain.repository.UsernameRepository

class SaveUsernameUseCase(private val usernameRepository: UsernameRepository) {
    suspend operator fun invoke(username: Username): Boolean{
        return usernameRepository.save(username)
    }
}