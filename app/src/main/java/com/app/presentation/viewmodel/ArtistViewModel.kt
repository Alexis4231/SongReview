package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.RetrofitClient
import com.app.domain.model.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {
    private val _artist = MutableStateFlow<Artist?>(null)
    val artist: StateFlow<Artist?> = _artist
    fun loadArtist(token: String){
        viewModelScope.launch {
            try{
                val artist = RetrofitClient.api.getArtistById(
                    authHeader = "Bearer $token",
                    artistId = "6eUKZXaKkcviH0Ku9w2n3V"
                )
                _artist.value = artist
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}