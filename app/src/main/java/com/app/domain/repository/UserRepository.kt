package com.app.domain.repository

import com.app.domain.model.User

interface UserRepository {
    suspend fun getByCode(code: String): User?
    suspend fun delete(code: String): Boolean
    suspend fun getEmailByName(name:String): String
    suspend fun updateFcmToken(): Boolean
}