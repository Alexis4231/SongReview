package com.app.domain.usecase.song

import com.app.domain.repository.SongRepository

class GetCodeByTitleAndArtistUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(title: String, artist: String): String?{
        return songRepository.getCodeByTitleAndArtist(title,artist)
    }
}