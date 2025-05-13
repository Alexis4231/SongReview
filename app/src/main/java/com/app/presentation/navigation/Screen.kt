package com.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object SuccesRegister: Screen("successregister")
    data object Home: Screen("home")
    data object Add: Screen("add")
    data object Reviews: Screen("reviews/{songCode}"){
        fun createRoute(songCode: String) = "reviews/$songCode"
    }
    data object Profile: Screen("profile")
    data object ResetPassword: Screen("resetpassword")
    data object SuccessResetPassword: Screen("successresetpassword")
}