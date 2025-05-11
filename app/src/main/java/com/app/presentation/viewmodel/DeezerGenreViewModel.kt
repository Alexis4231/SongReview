package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeezerGenreViewModel : ViewModel() {
    private val _genre = MutableStateFlow<Genre?>(null)
    val genre: StateFlow<Genre?> = _genre
    fun loadGenre(albumId: Int){
        viewModelScope.launch {
            try{
                val album = DeezerRetrofitClient.api.searchAlbumDetails(albumId)
                val firstGenre = album.genres.data.firstOrNull()
                _genre.value = firstGenre

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}