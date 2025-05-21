package com.app.domain.repository

import com.app.domain.model.Request

interface RequestRepository {
    suspend fun save(request: Request): Boolean
    suspend fun getRequestsByCodeIssuer(codeIssuer: String): List<Request>
    suspend fun getStatusByCodeIssuerAndCodeReceiver(codeIssuer: String, codeReceiver: String): String?
    suspend fun acceptRequest(codeIssuer: String, codeReceiver: String): Boolean
}