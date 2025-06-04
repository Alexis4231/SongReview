package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HotelClass
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.app.domain.model.User
import com.app.isInternetAvailable
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.ReviewViewModel
import com.app.presentation.viewmodel.SongDBViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.app.presentation.viewmodel.UsernameViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
    usernameViewModel: UsernameViewModel = koinViewModel(),
    reviewViewModel: ReviewViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    val reviews by reviewViewModel.reviews.collectAsState()
    val user = getUserDetailsViewModel.user.value

    LaunchedEffect(Unit) {
        getUserDetailsViewModel.loadUserData()
    }

    LaunchedEffect(user) {
        user?.let { reviewViewModel.getReviewsByCodeUser(it.code) }
    }

    var selectedProfileItem by remember { mutableStateOf(0) }
    val profileItems = listOf("Reseñas", "Ajustes")
    val profileIcons = listOf(Icons.Filled.HotelClass, Icons.Filled.Settings)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (user == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .zIndex(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF585D5F)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(
                    name = user.name,
                    email = user.email,
                    reviewsCount = reviews.size,
                    onFollowersClick = {
                        if(isInternetAvailable(context)) {
                            navController.navigate(Screen.Followers.route)
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar("Sin conexión a internet")
                            }
                        }
                    }
                )

                NavigationBar(containerColor = Color(0xFF39D0B9)) {
                    profileItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(profileIcons[index], contentDescription = item) },
                            label = { Text(item) },
                            selected = selectedProfileItem == index,
                            onClick = { selectedProfileItem = index },
                            modifier = Modifier.background(Color(0xFF39D0B9))
                        )
                    }
                }

                when (selectedProfileItem) {
                    0 -> ReviewsContent(navController, user, snackbarHostState, scope)
                    1 -> profileSettingsContent(navController, user, userViewModel,usernameViewModel, snackbarHostState, scope)
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    reviewsCount: Int,
    onFollowersClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF39D0B9))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = name, color = Color.White, fontSize = 18.sp)
            Text(text = email, color = Color.White, fontSize = 14.sp)
            Text(text = "$reviewsCount reseñas", color = Color.White, fontSize = 14.sp)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFD0C4FF), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.toUpperCase().firstOrNull()?.toString() ?: "",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .clickable { onFollowersClick() }
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    contentDescription = "Usuarios seguidos",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = "Usuarios\nseguidos", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ReviewsContent(
    navController: NavController,
    user: User,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    reviewViewModel: ReviewViewModel = koinViewModel(),
    songDBViewModel: SongDBViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    val reviews by reviewViewModel.reviews.collectAsState()
    val density = LocalDensity.current.density
    val isLoading by reviewViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit){
        reviewViewModel.getReviewsByCodeUser(user.code)
    }

    if(isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }else{
        if(reviews.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF585D5F)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(2f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mood,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Haz tu primer comentario",
                        color = Color.White,
                        fontSize = (10 * density).sp,
                        fontWeight = FontWeight.Bold,
                        )
                }
            }
        }else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(reviews) { review ->
                    var title by remember { mutableStateOf<String?>(null) }

                    LaunchedEffect(review.publicReview.codeSong) {
                        val song = songDBViewModel.fetchSongByCode(review.publicReview.codeSong)
                        title = song?.title ?: ""
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF44A898)),
                        onClick = {
                            if(isInternetAvailable(context)) {
                                navController.navigate(Screen.Reviews.createRoute(review.publicReview.codeSong))
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
                                color = Color(0xFFD0C4FF)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = user.name.toUpperCase().firstOrNull()?.toString()
                                            ?: "",
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title ?: "",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = user.name,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Row {
                                    repeat(5) { index ->
                                        Icon(
                                            imageVector = if (index < review.publicReview.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Star",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = review.publicReview.comment,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun profileSettingsContent(
    navController: NavController,
    user: User,
    userViewModel: UserViewModel,
    usernameViewModel: UsernameViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context = LocalContext.current
) {
    Card(
        modifier = Modifier
            .clickable { LogOut(navController) }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cerrar Sesión",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "LogOut",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    var showDeleteUserDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable {
                if(isInternetAvailable(context)) {
                    showDeleteUserDialog = true
                }else{
                    scope.launch {
                        snackbarHostState.showSnackbar("Sin conexión a internet")
                    }
                }
            }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Eliminar usuario",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.PersonOff,
                contentDescription = "Delete user",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if(showDeleteUserDialog){
        showDeleteUserPopup(
            onConfirm = {
                showDeleteUserDialog = false
                deleteUser(navController, user, userViewModel, usernameViewModel)
            },
            onDismiss = {
                showDeleteUserDialog = false
            }
        )
    }
}

fun LogOut(navController: NavController){
    FirebaseAuth.getInstance().signOut()
    navController.navigate("login"){
        popUpTo("profile") { inclusive = true }
    }
}

@Composable
fun showDeleteUserPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Tu cuenta será eliminada de forma irreversible") },
        text = { Text("¿Deseas eliminar tu cuenta?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


fun deleteUser(navController: NavController, userDelete: User, userViewModel: UserViewModel, usernameViewModel: UsernameViewModel){
    val user = FirebaseAuth.getInstance().currentUser
    usernameViewModel.deleteUser()
    userViewModel.deleteUser(userDelete.code)
    user?.delete()?.addOnCompleteListener{
        LogOut(navController)
    }
}