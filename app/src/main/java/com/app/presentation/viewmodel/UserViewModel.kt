package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.SongDB
import com.app.domain.model.User
import com.app.domain.usecase.review.GetReviewsUseCase
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.domain.usecase.user.GetEmailByNameUseCase
import com.app.domain.usecase.user.GetUserByCodeUseCase
import com.app.domain.usecase.user.UpdateFcmTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(
    private val getEmailByNameUseCase: GetEmailByNameUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserByCodeUseCase: GetUserByCodeUseCase,
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase
) : ViewModel() {

    private val _user = MutableStateFlow(User("", "", ""))
    val user: StateFlow<User> = _user

    fun deleteUser(code: String){
        viewModelScope.launch {
            deleteUserUseCase(code)
        }
    }

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

    fun getUserByCode(code: String){
        viewModelScope.launch {
            _user.value = getUserByCodeUseCase(code) ?: User("N/A","","")
        }
    }

    fun verifyAndUpdateToken(){
        viewModelScope.launch {
            updateFcmTokenUseCase()
        }
    }
}