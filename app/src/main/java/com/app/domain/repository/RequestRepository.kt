package com.app.domain.repository

interface RequestRepository {
    suspend fun save(usernameReceiver: String): Boolean
    suspend fun getStatus(username: String): Map<String,Boolean>?
}