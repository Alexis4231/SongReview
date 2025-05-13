package com.app.domain.usecase.song

import com.app.domain.model.SongDB
import com.app.domain.repository.SongRepository

class GetSongsUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(): List<SongDB>{
        return songRepository.getSongs()
    }
}