package com.app.presentation.ui.screens

import GetUserDetailsViewModel
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HotelClass
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.NavBarViewModel
import com.app.presentation.viewmodel.RequestViewModel
import com.app.presentation.viewmodel.ReviewViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardUserScreen(
    navController: NavController,
    username: String?,
    userViewModel: UserViewModel = koinViewModel(),
    reviewViewModel: ReviewViewModel = koinViewModel(),
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
    requestViewModel: RequestViewModel = koinViewModel()
) {
    val reviews by reviewViewModel.reviews.collectAsState()
    val user = userViewModel.user.collectAsState()
    val myUser = getUserDetailsViewModel.user.value
    val status by requestViewModel.status.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getUserDetailsViewModel.loadUserData()
    }

    LaunchedEffect(user,myUser) {
        if(myUser != null && user != null){
            requestViewModel.getStatusByCodeIssuerAndCodeReceiver(myUser.code,user.value.code)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserHeader(
            name = username.toString(),
            reviewsCount = reviews.size,
            status = status?.toString(),
            isLoading = isLoading,
            onFollowersClick = {
                isLoading = true
                myUser?.let { requestViewModel.setCodeIssuer(it.code) }
                requestViewModel.setCodeReceiver(user.value.code)
                requestViewModel.save()
                navController.navigate(Screen.CardUser.createRoute(user.value.code)) {
                    popUpTo(Screen.CardUser.createRoute(user.value.code)) { inclusive = true }
                }
            }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
        }
    }
}

@Composable
fun UserHeader(
    name: String,
    status: String?,
    isLoading: Boolean,
    reviewsCount: Int,
    onFollowersClick: () -> Unit
) {
    var colorButton = Color.Transparent
    var colorText = Color.Transparent
    var statusMessage = ""

    if(status.equals("PENDING")){
        colorButton = Color.Gray
        colorText = Color.White
        statusMessage = "Enviado"
    }else if(status.equals("ACCEPTED")){
        colorButton = Color.Gray
        colorText = Color.White
        statusMessage = "Siguiendo"
    }else{
        colorButton = Color.White
        colorText = Color.Black
        statusMessage = "Seguir"
    }

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
            Text(text = "$reviewsCount reseñas", color = Color.White, fontSize = 14.sp)
            status?.let { Text(text = it) }
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
            if(!isLoading) {
                Row(
                    modifier = Modifier
                        .clickable { onFollowersClick() }
                        .background(colorButton, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = statusMessage, color = colorText, fontSize = 12.sp)
                }
            }else{
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}