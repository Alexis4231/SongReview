package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.domain.model.Request
import com.app.domain.model.SongDB
import com.app.domain.usecase.request.GetStatusUseCase
import com.app.domain.usecase.request.SaveRequestUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestViewModel(
    private val saveRequestUseCase: SaveRequestUseCase,
    private val getStatusUseCase: GetStatusUseCase
):ViewModel() {
    private val _codeIssuer = MutableStateFlow("")
    val codeIssuer: StateFlow<String> = _codeIssuer.asStateFlow()

    private val _usernameReceiver = MutableStateFlow("")
    val usernameReceiver: StateFlow<String> = _usernameReceiver.asStateFlow()

    private val _status = MutableStateFlow<Map<String, Boolean>?>(null)
    val status: StateFlow<Map<String,Boolean>?> = _status.asStateFlow()

    private val _isStatusLoading = MutableStateFlow(false)
    val isStatusLoading: StateFlow<Boolean> = _isStatusLoading.asStateFlow()

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests.asStateFlow()

    fun setCodeIssuer(codeIssuer: String){
        _codeIssuer.value = codeIssuer
    }

    fun setUsernameReceiver(usernameReceiver: String){
        _usernameReceiver.value = usernameReceiver
    }

    fun save(){
        viewModelScope.launch {
            saveRequestUseCase(usernameReceiver = _usernameReceiver.value)
        }
    }

    fun getStatus(username: String){
        viewModelScope.launch {
            _isStatusLoading.value = true
            _status.value = getStatusUseCase(username)
            _isStatusLoading.value = false
        }
    }
}