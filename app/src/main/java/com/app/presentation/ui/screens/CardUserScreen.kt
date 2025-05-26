package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.R
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.NavBarViewModel
import com.app.presentation.viewmodel.RequestViewModel
import com.app.presentation.viewmodel.ReviewViewModel
import com.app.presentation.viewmodel.SongDBViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CardUserScreen(
    navController: NavController,
    username: String?,
    reviewViewModel: ReviewViewModel = koinViewModel(),
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
    requestViewModel: RequestViewModel = koinViewModel(),
    songDBViewModel: SongDBViewModel = koinViewModel()
) {
    val reviews by reviewViewModel.publicReviews.collectAsState()
    val myUser = getUserDetailsViewModel.user.value
    val statusMap by requestViewModel.status.collectAsState()
    var showDeleteRequestDialog by remember { mutableStateOf(false) }
    var showCancelRequestDialog by remember { mutableStateOf(false) }

    val density = LocalDensity.current.density
    val status = statusMap?.keys?.firstOrNull() ?: ""
    val isSender = statusMap?.values?.firstOrNull() ?: false

    LaunchedEffect(Unit) {
        getUserDetailsViewModel.loadUserData()
        username?.let { requestViewModel.getStatus(it) }
        username?.let { reviewViewModel.getReviewsByUsernameFollower(it) }
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
            status = status,
            isSender = isSender,
            onSendRequest = {
                myUser?.let{
                    requestViewModel.setCodeIssuer(it.code)
                    requestViewModel.setUsernameReceiver(username ?: "")
                    CoroutineScope(Dispatchers.Main).launch {
                        requestViewModel.save()
                        delay(500)
                        requestViewModel.getStatus(username ?: "")
                    }
                }
            },
            onAcceptRequest = {
                CoroutineScope(Dispatchers.Main).launch {
                    requestViewModel.acceptRequest(username ?: "")
                    delay(500)
                    requestViewModel.getStatus(username ?: "")
                    reviewViewModel.getReviewsByUsernameFollower(username ?: "")

                }
            },
            onDeleteRequest = {
                showDeleteRequestDialog = true
            },
            onCancelRequest = {
                showCancelRequestDialog = true
            }
        )

        when (status){
            "ACCEPTED" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews) { review ->
                        var title by remember { mutableStateOf<String?>(null) }
                            LaunchedEffect(review.codeSong){
                                val song = songDBViewModel.fetchSongByCode(review.codeSong)
                                title = song?.title ?: ""
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF44A898)),
                                onClick = { navController.navigate(Screen.Reviews.createRoute(review.codeSong)) }
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
                                                text = username?.toUpperCase()?.firstOrNull()?.toString() ?: "",
                                                color = Color.Black
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = title ?: "", fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 20.sp)
                                        username?.let {
                                            Text(
                                                text = it,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White
                                            )
                                        }
                                        Row {
                                            repeat(5) { index ->
                                                Icon(
                                                    imageVector = if (index < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                                    contentDescription = "Star",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = review.comment,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                "PENDING" -> {
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
                            Image(
                                painter = painterResource(id = R.drawable.lock),
                                contentDescription = "Logo",
                                modifier = Modifier.fillMaxSize().padding(0.dp, 10.dp, 0.dp, 0.dp)
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
                                text = "Sigue a este usuario para ver su reseñas",
                                color = Color.Black,
                                fontSize = (10 * density).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
                else -> {
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
                            Image(
                                painter = painterResource(id = R.drawable.lock),
                                contentDescription = "Logo",
                                modifier = Modifier.fillMaxSize().padding(0.dp, 10.dp, 0.dp, 0.dp)
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
                                text = "Sigue a este usuario para ver su reseñas",
                                color = Color.Black,
                                fontSize = (10 * density).sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }
            }
        }
        if(showDeleteRequestDialog){
            showDeleteRequestPopup(
                onConfirm = {
                    showDeleteRequestDialog = false
                    CoroutineScope(Dispatchers.Main).launch {
                        requestViewModel.deleteRequest(username ?: "")
                        delay(500)
                        requestViewModel.getStatus(username ?: "")
                    }
                },
                onDismiss = {
                    showDeleteRequestDialog = false
                }
            )
        }
        if(showCancelRequestDialog){
            showCancelRequestPopup(
                onConfirm = {
                    showCancelRequestDialog = false
                    CoroutineScope(Dispatchers.Main).launch {
                        requestViewModel.cancelRequest(username ?: "")
                        delay(500)
                        requestViewModel.getStatus(username ?: "")
                    }
                },
                onDismiss = {
                    showCancelRequestDialog = false
                }
            )
        }
    }

@Composable
fun UserHeader(
    name: String,
    status: String?,
    isSender: Boolean,
    reviewsCount: Int,
    onSendRequest: () -> Unit,
    onAcceptRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onCancelRequest: () -> Unit,
    requestViewModel: RequestViewModel = koinViewModel()
) {
    val statusLoading by requestViewModel.isStatusLoading.collectAsState()

    val (colorButton, colorText, statusMessage) = when (status){
        "PENDING" -> if(isSender){
            Triple(Color.Gray, Color.White, "Enviado")
        }else{
            Triple(Color.White,Color.Black,"Aceptar")
        }
        "ACCEPTED" -> Triple(Color.Gray, Color.White, "Siguiendo")
        else -> Triple(Color.White,Color.Black,"Seguir")
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
            if(status.equals("ACCEPTED")) {
                Text(text = "$reviewsCount reseñas", color = Color.White, fontSize = 14.sp)
            }
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
            if(!statusLoading) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            when (status){
                                "PENDING" -> if(!isSender){
                                    onAcceptRequest()
                                    }else{
                                        onCancelRequest()
                                    }
                                "ACCEPTED" -> {
                                    onDeleteRequest()
                                }
                                else -> {
                                    onSendRequest()
                                }
                            }
                        }
                        .background(colorButton, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = statusMessage, color = colorText, fontSize = 12.sp)
                }
            }else{
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Composable
fun showDeleteRequestPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Dejar de seguir") },
        text = { Text("¿Deseas dejar de seguir a este usuario?") },
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

@Composable
fun showCancelRequestPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Cancelar solicitud") },
        text = { Text("¿Deseas eliminar la solicitud de amistad?") },
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