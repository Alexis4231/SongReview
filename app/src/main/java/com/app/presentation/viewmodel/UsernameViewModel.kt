package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Username
import com.app.domain.usecase.username.DeleteUsernameUseCase
import com.app.domain.usecase.username.ExistUsernameUseCase
import com.app.domain.usecase.username.GetUsernamesUseCase
import com.app.domain.usecase.username.SaveUsernameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsernameViewModel(
    private val saveUsernameUseCase: SaveUsernameUseCase,
    private val existUsernameUseCase: ExistUsernameUseCase,
    private val deleteUsernameUseCase: DeleteUsernameUseCase,
    private val getUsernamesUseCase: GetUsernamesUseCase
): ViewModel() {
    private val _username = MutableStateFlow(Username("","",""))
    val username: StateFlow<Username> = _username

    private val _names = MutableStateFlow<List<String>>(emptyList())
    val names: StateFlow<List<String>> = _names.asStateFlow()

    fun setName(name: String){
        _username.value = _username.value.copy(
            name = name
        )
    }

    fun setCodeUser(codeUser: String){
        _username.value = _username.value.copy(
            codeUser = codeUser
        )
    }

    fun save(){
        viewModelScope.launch {
            saveUsernameUseCase(username.value)
        }
    }

    fun getUsernames(){
        viewModelScope.launch {
            _names.value = getUsernamesUseCase()
        }
    }

    fun avaliableUsername(name: String, onSuccess: () -> Unit, onFail : () -> Unit) {
        viewModelScope.launch {
            if (!existUsernameUseCase(name)) {
                onSuccess()
            } else {
                onFail()
            }
        }
    }

    fun deleteUser(){
        viewModelScope.launch {
            deleteUsernameUseCase()
        }
    }
}