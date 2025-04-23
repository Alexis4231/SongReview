package com.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object SuccesRegister: Screen("successregister")
    data object Home: Screen("home")
    data object ResetPassword: Screen("resetpassword")
    data object SuccessResetPassword: Screen("successresetpassword")
}