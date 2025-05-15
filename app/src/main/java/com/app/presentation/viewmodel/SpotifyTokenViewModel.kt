package com.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.retrofit.SpotifyRetrofitAuth
import com.app.domain.model.AccessTokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpotifyTokenViewModel : ViewModel() {
    private val _token = MutableStateFlow<AccessTokenResponse?>(null)
    val token: StateFlow<AccessTokenResponse?> = _token
    fun loadToken(){
        viewModelScope.launch {
            try{
                val token = SpotifyRetrofitAuth.api.getToken()
                _token.value = token
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}