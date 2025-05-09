package com.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Album
import com.app.domain.model.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeezerAlbumsViewModel: ViewModel() {
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    fun loadAlbums(albumId: Int){
        viewModelScope.launch {
            try{
                val albumsResponse = DeezerRetrofitClient.api.searchAlbums(albumId)
                _albums.value = albumsResponse.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}