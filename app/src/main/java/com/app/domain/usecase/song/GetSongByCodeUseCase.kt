package com.app.domain.usecase.song

import com.app.domain.model.SongDB
import com.app.domain.repository.SongRepository

class GetSongByCodeUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(code: String): SongDB? {
        return songRepository.getByCode(code)
    }
}