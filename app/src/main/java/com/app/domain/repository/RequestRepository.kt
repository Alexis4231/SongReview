package com.app.domain.repository

interface RequestRepository {
    suspend fun save(usernameReceiver: String): Boolean
    suspend fun getStatus(username: String): Map<String,Boolean>?
    suspend fun acceptRequest(username: String): Boolean
    suspend fun deleteRequest(username: String): Boolean
    suspend fun cancelRequest(username: String): Boolean
    suspend fun getFollowers(): List<String>
}