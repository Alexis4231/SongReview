package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.SongDB
import com.app.domain.usecase.song.SaveSongUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongDBViewModel(
    private val saveSongUseCase: SaveSongUseCase
): ViewModel() {
    private val _song = MutableStateFlow(SongDB("","","",""))
    val song: StateFlow<SongDB> = _song

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
}