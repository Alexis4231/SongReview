package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongsViewModel : ViewModel() {
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    var currentPage = 1

    fun loadSongs(search: String, page: Int){
        viewModelScope.launch {
            try{
                val songsResponse = DeezerRetrofitClient.api.searchSongs(search)
                _songs.value = songsResponse.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}