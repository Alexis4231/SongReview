package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.YoutubeRetrofitClient
import com.app.domain.model.YoutubeLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class YoutubeLinkViewModel : ViewModel() {
    private val _youtubeLink = MutableStateFlow<YoutubeLink?>(null)
    val youtubeLink: StateFlow<YoutubeLink?> = _youtubeLink

    fun loadLink(search: String){
        viewModelScope.launch {
            val songsResponse = YoutubeRetrofitClient.api.searchSong(query = search)
            val item = songsResponse.items.firstOrNull()
            _youtubeLink.value = item?.id
        }
    }

}