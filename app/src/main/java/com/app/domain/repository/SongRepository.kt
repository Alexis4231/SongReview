package com.app.domain.repository

import com.app.domain.model.SongDB

interface SongRepository {
    suspend fun save(song: SongDB): Boolean
    suspend fun getSongs(): List<SongDB>
    suspend fun getByCode(code: String): SongDB?
}