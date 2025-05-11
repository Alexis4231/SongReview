package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HotelClass
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.domain.model.User
import com.app.presentation.viewmodel.NavBarViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    navBarViewModel: NavBarViewModel,
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        getUserDetailsViewModel.loadUserData()
    }

    val user = getUserDetailsViewModel.user.value

    var selectedProfileItem by remember { mutableStateOf(0) }
    val profileItems = listOf("Reseñas", "Ajustes")
    val profileIcons = listOf(Icons.Filled.HotelClass, Icons.Filled.Settings)

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF585D5F)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(
                name = user.name,
                email = user.email,
                reviewsCount = 3,
                onFollowersClick = {
                    println("Usuarios seguidos clickeado")
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
                0 -> ReviewsContent()
                1 -> profileSettingsContent(navController,  user, userViewModel)
            }
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    reviewsCount: Int,
    onFollowersClick: () -> Unit
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
fun ReviewsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(50) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Item #$index",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun profileSettingsContent(navController: NavController, user: User, userViewModel: UserViewModel) {
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

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable {showDialog = true}
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

    if(showDialog){
        showDeletePopup(
            onConfirm = {
                showDialog = false
                deleteUser(navController, user, userViewModel)
            },
            onDismiss = {
                showDialog = false
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
fun showDeletePopup(
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


fun deleteUser(navController: NavController, userDelete: User, userViewModel: UserViewModel){
    val user = FirebaseAuth.getInstance().currentUser
    user?.delete()?.addOnCompleteListener{task ->
        if(task.isSuccessful){
            userViewModel.deleteUser(userDelete.code)
            Log.d("User","Usuario eliminado exitosamente")
            LogOut(navController)
        }else{
            Log.e("User","Error al eliminar el usuario")
        }
    }
}