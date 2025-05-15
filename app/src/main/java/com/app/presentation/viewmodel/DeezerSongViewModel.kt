package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeezerSongViewModel : ViewModel() {
    private val _song = MutableStateFlow<Song?>(null)
    val song: StateFlow<Song?> = _song

    fun loadSong(search: String){
        viewModelScope.launch {
            try{
                val songsResponse = DeezerRetrofitClient.api.searchSongs(search)
                _song.value = songsResponse.data.firstOrNull()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}