package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.SongDB
import com.app.domain.usecase.song.GetSongByCodeUseCase
import com.app.domain.usecase.song.GetSongsUseCase
import com.app.domain.usecase.song.SaveSongUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongDBViewModel(
    private val saveSongUseCase: SaveSongUseCase,
    private val getSongsUseCase: GetSongsUseCase,
    private val getSongByCodeUseCase: GetSongByCodeUseCase
): ViewModel() {
    private val _song = MutableStateFlow(SongDB("","","",""))
    val song: StateFlow<SongDB> = _song

    private val _songs = MutableStateFlow<List<SongDB>>(emptyList())
    val songs: StateFlow<List<SongDB>> = _songs.asStateFlow()

    fun setTitle(title: String) {
        _song.value = _song.value.copy(
            title = title
        )
    }

    fun setArtist(artist: String){
        _song.value = _song.value.copy(
            artist = artist
        )
    }

    fun setGenre(genre: String){
        _song.value = _song.value.copy(
            genre = genre
        )
    }

    fun save() {
        viewModelScope.launch {
            saveSongUseCase(song.value)
        }
    }

    fun getSongs(){
        viewModelScope.launch {
            _songs.value = getSongsUseCase()
        }
    }

    fun getSongByCode(code: String){
        viewModelScope.launch {
            _song.value = getSongByCodeUseCase(code) ?: SongDB("N/A","no encontrado","no encontrado","no encontrado")
        }
    }
}