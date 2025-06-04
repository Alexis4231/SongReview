package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.SpotifyRetrofitClient
import com.app.domain.model.SpotifyLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpotifyLinkViewModel : ViewModel() {
    private val _spotifyLink = MutableStateFlow<SpotifyLink?>(null)
    val spotifyLink: StateFlow<SpotifyLink?> = _spotifyLink

    fun loadLink(token: String, search: String){
        viewModelScope.launch {
            try{
                val auth = "Bearer $token"
                val songsResponse = SpotifyRetrofitClient.api.searchSong(auth,search)
                val item = songsResponse.tracks.items.firstOrNull()
                _spotifyLink.value = item?.external_urls
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}