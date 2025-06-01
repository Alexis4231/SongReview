package com.app.domain.repository

import com.app.domain.model.Username

interface UsernameRepository {
    suspend fun save(username: Username): Boolean
    suspend fun existUsername(name: String): Boolean
    suspend fun getUsernames(): List<String>
    suspend fun delete(): Boolean
}