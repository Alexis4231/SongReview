package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistsViewModel : ViewModel() {
    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists

    var currentPage = 1

    fun loadArtists(search: String, page: Int){
        viewModelScope.launch {
            try{
                val artistsResponse = DeezerRetrofitClient.api.searchArtists(search)
                _artists.value = artistsResponse.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}