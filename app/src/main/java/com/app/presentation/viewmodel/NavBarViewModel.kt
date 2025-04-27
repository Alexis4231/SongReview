package com.app.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NavBarViewModel: ViewModel() {
    val isVisible = mutableStateOf(true)

    fun show(){
        isVisible.value = true
    }

    fun hide(){
        isVisible.value = false
    }
}