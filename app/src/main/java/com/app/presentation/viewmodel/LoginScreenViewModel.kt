package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.User
import com.app.domain.usecase.user.GetEmailByNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginScreenViewModel(
    private val getEmailByNameUseCase: GetEmailByNameUseCase,
) : ViewModel() {

    private val _user = MutableStateFlow(User("", "", ""))
    val user: StateFlow<User> = _user

    fun getEmail(name: String,result: (String?) -> Unit){
        viewModelScope.launch {
            val email = getEmailByNameUseCase(name)
            if(email.isEmpty()){
                result(null)
            }else{
                result(email)
            }
        }
    }
}