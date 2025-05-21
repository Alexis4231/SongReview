package com.app.domain.usecase.request

import com.app.domain.model.Request
import com.app.domain.repository.RequestRepository

class GetRequestsByCodeIssuerUseCase(private val requestRepository: RequestRepository){
    suspend operator fun invoke(codeIssuer: String): List<Request>{
        return requestRepository.getRequestsByCodeIssuer(codeIssuer)
    }
}