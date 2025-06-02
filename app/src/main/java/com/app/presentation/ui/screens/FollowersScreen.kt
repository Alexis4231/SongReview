package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.app.isInternetAvailable
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.RequestViewModel
import com.app.presentation.viewmodel.ReviewViewModel
import com.app.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FollowersScreen(
    navController: NavController,
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
    reviewViewModel: ReviewViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    var selectedProfileItem by remember { mutableStateOf(0) }
    var search by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Todos") }
    val profileItems = listOf("Seguidores","Solicitudes")
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }



    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .zIndex(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp)
                .background(Color(0xFF585D5F)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFF39D0B9)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                        .background(Color(0xFF39D0B9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Usuarios seguidos",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                }
            }
            NavigationBar(containerColor = Color(0xFF44A898)) {
                profileItems.forEachIndexed { index, item ->
                    NavigationBar(containerColor = Color(0xFF4FB3A4)) {
                        profileItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = item,
                                            color = Color.White
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .height(2.dp)
                                                .width(24.dp)
                                                .background(
                                                    if (selectedProfileItem == index) Color.White else Color.Transparent
                                                )
                                        )
                                    }
                                },
                                selected = selectedProfileItem == index,
                                onClick = { selectedProfileItem = index },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                    selectedIconColor = Color.White,
                                    unselectedIconColor = Color.LightGray,
                                    selectedTextColor = Color.White,
                                    unselectedTextColor = Color.LightGray
                                ),
                                modifier = Modifier
                                    .background(Color(0xFF4FB3A4))
                            )
                        }
                    }
                }
            }

            when (selectedProfileItem) {
                0 -> followersContent(navController, snackbarHostState, scope)
                1 -> requestsContent(navController, snackbarHostState, scope)
            }
        }
    }
}

@Composable
fun followersContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    requestViewModel: RequestViewModel = koinViewModel(),
    context: Context = LocalContext.current
){
    val usernames by requestViewModel.usernames.collectAsState()
    val density = LocalDensity.current.density
    val loading by requestViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        requestViewModel.getFollowers()
    }

    if(loading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }else{
        if (usernames.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF585D5F)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = "GroupAdd",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Los usuarios que sigas aparecerán aquí",
                        color = Color.White,
                        fontSize = (10 * density).sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            for (username in usernames) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(usernames) { username ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF44A898)),
                            onClick = {
                                if(isInternetAvailable(context)) {
                                    navController.navigate(Screen.CardUser.createRoute(username))
                                }else{
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Sin conexión a internet")
                                    }
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = Color.White
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = username.toUpperCase().firstOrNull()?.toString()
                                                ?: "",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = username,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun requestsContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    requestViewModel: RequestViewModel = koinViewModel(),
    context: Context = LocalContext.current
){
    val usernames by requestViewModel.usernames.collectAsState()
    val density = LocalDensity.current.density
    val loading by requestViewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        requestViewModel.getRequestFollowers()
    }
    if(loading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }else {
        if (usernames.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF585D5F)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "No tienes solicitudes de seguimiento pendientes",
                        color = Color.White,
                        fontSize = (10 * density).sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            for (username in usernames) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(usernames) { username ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF44A898)),
                            onClick = {
                                if(isInternetAvailable(context)) {
                                    navController.navigate(Screen.CardUser.createRoute(username))
                                }else{
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Sin conexión a internet")
                                    }
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = Color.White
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = username.toUpperCase().firstOrNull()?.toString()
                                                ?: "",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = username,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}