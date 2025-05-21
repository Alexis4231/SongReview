package com.app.domain.usecase.request

import com.app.domain.repository.RequestRepository

class GetStatusByCodeIssuerAndCodeReceiverUseCase(private val requestRepository: RequestRepository){
    suspend operator fun invoke(codeIssuer: String,codeReceiver: String): String?{
        return requestRepository.getStatusByCodeIssuerAndCodeReceiver(codeIssuer,codeReceiver)
    }
}