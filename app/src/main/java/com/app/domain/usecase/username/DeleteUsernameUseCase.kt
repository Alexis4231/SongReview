package com.app.domain.usecase.username

import com.app.domain.repository.UsernameRepository

class DeleteUsernameUseCase(private val usernameRepository: UsernameRepository){
    suspend operator fun invoke(): Boolean{
        return usernameRepository.delete()
    }
}