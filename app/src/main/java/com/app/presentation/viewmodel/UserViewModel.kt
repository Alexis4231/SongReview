package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.SongDB
import com.app.domain.model.User
import com.app.domain.usecase.review.GetReviewsUseCase
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.domain.usecase.user.ExistUsernameUseCase
import com.app.domain.usecase.user.GetEmailByNameUseCase
import com.app.domain.usecase.user.GetUserByCodeUseCase
import com.app.domain.usecase.user.GetUsersUseCase
import com.app.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val existUsernameUseCase: ExistUsernameUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val getEmailByNameUseCase: GetEmailByNameUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getUserByCodeUseCase: GetUserByCodeUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _user = MutableStateFlow(User("", "", ""))
    val user: StateFlow<User> = _user

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()


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

    fun deleteUser(code: String){
        viewModelScope.launch {
            deleteUserUseCase(code)
        }
    }

    fun getUsers(){
        viewModelScope.launch {
            _users.value = getUsersUseCase()
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
}