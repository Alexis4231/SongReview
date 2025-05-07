package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.DeezerRetrofitClient
import com.app.domain.model.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeezerViewModel : ViewModel() {
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    fun loadGenres(){
        viewModelScope.launch {
            try{
                val genresResponse = DeezerRetrofitClient.api.getGenres()
                val genres = genresResponse.data.drop(1)
                _genres.value = genres
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}