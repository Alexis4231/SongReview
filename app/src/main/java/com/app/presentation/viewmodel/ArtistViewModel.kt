package com.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.app.data.remote.RetrofitInstance
import com.app.domain.model.Artist
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {
    private val _artists = mutableStateOf<List<Artist>>(emptyList())
    val artists: State<List<Artist>> = _artists

    private val token = "Bearer BQAnnxSfz0lPsZTESSDtq19BqGF9hRzJwE0LiE1qzJ38UzFFwk-Ooer3UW9oz7l1Q1b_aAfN3v1yH6A72R29ujZTDJ-gdKeRfdvRjuqfXp9qU5YzJIuPbZkCqLWtv8gTI-Zxxlz1gVE"
    fun searchArtist(query: String){
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.api.searchArtists(token = token, query = query, limit = 50)
                _artists.value = response.artists.items
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}