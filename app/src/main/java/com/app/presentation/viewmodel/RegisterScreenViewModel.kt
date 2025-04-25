package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.User
import com.app.domain.usecase.user.ExistUsernameUseCase
import com.app.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class RegisterScreenViewModel(
    private val existUsernameUseCase: ExistUsernameUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _user = MutableStateFlow(User("", "", ""))
    val user: StateFlow<User> = _user

    fun setName(name: String) {
        _user.value = _user.value.copy(
            name = name
        )
    }

    fun setEmail(email:String){
        _user.value = _user.value.copy(
            email = email
        )
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

    fun save() {
        viewModelScope.launch {
            saveUserUseCase(user.value)
        }
    }
}