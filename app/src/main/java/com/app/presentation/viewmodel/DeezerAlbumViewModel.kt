package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Album
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeezerAlbumViewModel: ViewModel() {
    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album
    fun loadAlbum(artistId: Int){
        viewModelScope.launch {
            try{
                val album = DeezerRetrofitClient.api.searchAlbum(artistId, limit = 1)
                val firstAlbum = album.data.firstOrNull()
                _album.value = firstAlbum
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}