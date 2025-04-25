package com.app.presentation.ui.screens

import HomeScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.presentation.navigation.Screen
import com.app.presentation.ui.screens.Login.LoginScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if(currentRoute != Screen.Login.route) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.Home.route){ HomeScreen(navController) }
            composable(Screen.Add.route){ AddScreen() }
            composable(Screen.Search.route){ SearchScreen() }
            composable(Screen.Profile.route){ ProfileScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController){
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val items = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.Add to Icons.Default.Add,
        Screen.Search to Icons.Default.Search,
        Screen.Profile to Icons.Default.PersonOutline
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        items.forEach{ (screen, icon) ->
            BottomNavigationItem(
                icon = {Icon(imageVector = icon, contentDescription = screen.route)},
                label = { Text(screen.route)},
                selected = currentRoute == screen.route,
                onClick = {
                    if(currentRoute != screen.route){
                        navController.navigate(screen.route){
                            popUpTo(navController.graph.startDestinationId){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = true
            )
        }
    }




    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            route = Screen.Home.route,
            currentRoute = currentRoute,
            navController = navController
        )
        NavigationItem(
            icon = Icons.Default.Add,
            label = "Add",
            route = Screen.Add.route,
            currentRoute = currentRoute,
            navController = navController
        )
        NavigationItem(
            icon = Icons.Default.Search,
            label = "Buscar",
            route = Screen.Search.route,
            currentRoute = currentRoute,
            navController = navController
        )
        NavigationItem(
            icon = Icons.Default.PersonOutline,
            label = "Perfil",
            route = Screen.Profile.route,
            currentRoute = currentRoute,
            navController = navController
        )
    }
}

@Composable
fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    route: String,
    currentRoute: String?,
    navController: NavController
) {
    IconButton(
        onClick = {
            if (currentRoute != route) {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (currentRoute == route) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
            Text(label, style = MaterialTheme.typography.body2)
        }
    }
}