package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Request
import com.app.domain.model.SongDB
import com.app.domain.usecase.request.GetRequestsByCodeIssuerUseCase
import com.app.domain.usecase.request.GetStatusByCodeIssuerAndCodeReceiverUseCase
import com.app.domain.usecase.request.SaveRequestUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestViewModel(
    private val saveRequestUseCase: SaveRequestUseCase,
    private val getRequestsByCodeIssuerUseCase: GetRequestsByCodeIssuerUseCase,
    private val getStatusByCodeIssuerAndCodeReceiverUseCase: GetStatusByCodeIssuerAndCodeReceiverUseCase
):ViewModel() {
    private val _request = MutableStateFlow(Request("","","","PENDING"))
    val request: StateFlow<Request> = _request

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests.asStateFlow()

    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status.asStateFlow()

    fun setCodeIssuer(codeIssuer: String) {
        _request.value = _request.value.copy(
            codeIssuer = codeIssuer
        )
    }

    fun setCodeReceiver(codeReceiver: String) {
        _request.value = _request.value.copy(
            codeReceiver = codeReceiver
        )
    }

    fun save(){
        viewModelScope.launch {
            saveRequestUseCase(request.value)
        }
    }

    fun getRequestsByCodeIssuer(codeIssuer: String){
        viewModelScope.launch {
            _requests.value = getRequestsByCodeIssuerUseCase(codeIssuer)
        }
    }

    fun getStatusByCodeIssuerAndCodeReceiver(codeIssuer: String,codeReceiver: String){
        viewModelScope.launch {
            val result = getStatusByCodeIssuerAndCodeReceiverUseCase(codeIssuer,codeReceiver)
            _status.value = result
        }
    }
}