package com.app.domain.repository

import com.app.domain.model.User

interface UserRepository {
    suspend fun getByCode(code: String): User?
    suspend fun save(user:User): Boolean
    suspend fun delete(code: String): Boolean
    suspend fun existUsername(name: String): Boolean
    suspend fun getEmailByName(name:String): String
    suspend fun getUsers(): List<User>
}