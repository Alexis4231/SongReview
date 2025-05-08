package com.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.SpotifyRetrofitAuth
import com.app.domain.model.AccessTokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpotifyViewModel : ViewModel() {
    private val _token = MutableStateFlow<AccessTokenResponse?>(null)
    val token: StateFlow<AccessTokenResponse?> = _token
    fun loadToken(){
        viewModelScope.launch {
            try{
                val token = SpotifyRetrofitAuth.api.getToken()
                Log.d("SpotifyViewModel", "Token recibido: ${token.access_token}")
                _token.value = token
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}