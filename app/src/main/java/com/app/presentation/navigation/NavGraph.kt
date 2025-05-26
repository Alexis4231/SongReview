package com.app.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.presentation.ui.screens.AddScreen
import com.app.presentation.ui.screens.CardUserScreen
import com.app.presentation.ui.screens.FollowersScreen
import com.app.presentation.ui.screens.HomeScreen
import com.app.presentation.ui.screens.Login.LoginScreen
import com.app.presentation.ui.screens.Login.RegisterScreen
import com.app.presentation.ui.screens.Login.ResetPasswordScreen
import com.app.presentation.ui.screens.Login.SuccessRegisterScreen
import com.app.presentation.ui.screens.Login.SuccessResetPasswordScreen
import com.app.presentation.ui.screens.ProfileScreen
import com.app.presentation.ui.screens.PruebaScreen
import com.app.presentation.ui.screens.ReviewsScreen
import com.app.presentation.ui.screens.SongAdded
import com.app.presentation.ui.screens.SongNotAdded
import com.app.presentation.ui.screens.SplashScreen
import com.app.presentation.viewmodel.NavBarViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navBarViewModel: NavBarViewModel = viewModel()) {
    val screensWithoutNavBar = listOf(
        Screen.Login.route,
        Screen.Register.route,
        Screen.SuccesRegister.route,
        Screen.ResetPassword.route,
        Screen.SuccessResetPassword.route,
        Screen.Splash.route
    )
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUiController.setSystemBarsColor(Color.Black)
        systemUiController.setNavigationBarColor(Color.Black)
    }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding(),
        bottomBar = {
            if (currentRoute !in screensWithoutNavBar) {
                BottomNavigationBar(navController = navController, navBarViewModel)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Prueba.route,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(Screen.Splash.route){ SplashScreen(navController) }
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.Register.route) { RegisterScreen(navController) }
            composable(Screen.SuccesRegister.route) { SuccessRegisterScreen(navController) }
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Add.route) { AddScreen(navController) }
            composable(Screen.Reviews.route) { backStackEntry ->
                val code = backStackEntry.arguments?.getString("songCode")
                ReviewsScreen(navController, code)
            }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.ResetPassword.route) { ResetPasswordScreen(navController) }
            composable(Screen.SuccessResetPassword.route) { SuccessResetPasswordScreen(navController) }
            composable(Screen.SongAdded.route) {backStackEntry ->
                val name = backStackEntry.arguments?.getString("songName")
                val artist = backStackEntry.arguments?.getString("songArtist")
                SongAdded(navController,name,artist)
            }
            composable(Screen.SongNotAdded.route) {backStackEntry ->
                val name = backStackEntry.arguments?.getString("songName")
                val artist = backStackEntry.arguments?.getString("songArtist")
                SongNotAdded(navController,name,artist)
            }
            composable(Screen.CardUser.route) {backStackEntry ->
                val username = backStackEntry.arguments?.getString("username")
                CardUserScreen(navController,username)
            }
            composable(Screen.Followers.route){ FollowersScreen(navController) }

            composable(Screen.Prueba.route){ PruebaScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, navBarViewModel: NavBarViewModel) {
    val isVisible by navBarViewModel.isVisible
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val items = listOf(
        Triple(Screen.Home, Icons.Default.MusicNote,"Inicio"),
        Triple(Screen.Add, Icons.Default.Add,"Añadir"),
        Triple(Screen.Profile, Icons.Default.Person, "Perfil")
    )
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) { }
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.Black,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (screen, icon, label) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(currentRoute!!) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val scale by animateFloatAsState(
                        targetValue = if (currentRoute == screen.route) 1.2f else 1f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val fontSize by animateFloatAsState(
                        targetValue = if (currentRoute == screen.route) 14f else 12f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = screen.route,
                        modifier = Modifier.scale(scale),
                        tint = if (currentRoute == screen.route)
                            Color.White
                        else
                            Color.Gray
                    )
                    Text(
                        text = label,
                        fontSize = fontSize.sp,
                        color = if (currentRoute == screen.route)
                            Color.White
                        else
                            Color.Gray
                    )
                }
            }
        }
    }
}
