package com.app.domain.usecase.song

import com.app.domain.model.SongDB
import com.app.domain.repository.SongRepository

class SaveSongUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(song: SongDB): Boolean{
        return songRepository.save(song)
    }
}