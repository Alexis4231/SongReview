package com.app.domain.repository

import com.app.domain.model.Review

interface ReviewRepository {
    suspend fun save(review: Review): Boolean
}