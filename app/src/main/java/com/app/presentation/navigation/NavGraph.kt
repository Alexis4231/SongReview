package com.app.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.presentation.ui.screens.HomeScreen
import com.app.presentation.ui.screens.Login.LoginScreen
import com.app.presentation.ui.screens.Login.RegisterScreen
import com.app.presentation.ui.screens.Login.ResetPasswordScreen
import com.app.presentation.ui.screens.Login.SuccessRegisterScreen
import com.app.presentation.ui.screens.Login.SuccessResetPasswordScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination){
        composable(Screen.Login.route){
            LoginScreen(navController)
        }
        composable(Screen.Register.route){
            RegisterScreen(navController)
        }
        composable(Screen.Home.route){
            HomeScreen(navController)
        }
        composable(Screen.SuccesRegister.route) {
            SuccessRegisterScreen(navController)
        }
        composable(Screen.ResetPassword.route){
            ResetPasswordScreen(navController)
        }
        composable(Screen.SuccessResetPassword.route){
            SuccessResetPasswordScreen(navController)
        }
    }
}