package com.app.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.app.isInternetAvailable
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.SongDBViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongAdded(
    navController: NavController,
    songName: String?,
    artistName: String?,
    songDBViewModel: SongDBViewModel = koinViewModel(),
    context: Context = LocalContext.current
){
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current.density
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    val code by songDBViewModel.code.collectAsState()

    LaunchedEffect(Unit) {
        if(songName != null && artistName != null) {
            delay(2000)
            songDBViewModel.getCodeByTitleAndArtist(songName, artistName)
        }
    }

    if(code != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .zIndex(1f)
            )
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
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = screenWidth * 0.07f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Canción añadida correctamente",
                        color = Color.White,
                        fontSize = (8.5 * density).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Gracias por añadir una nueva canción",
                        color = Color.White,
                        fontSize = (6 * density).sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = screenWidth * 0.07f),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (!code.isNullOrEmpty()) {
                                if(isInternetAvailable(context)) {
                                    navController.navigate(Screen.Reviews.createRoute(code!!)) {
                                        popUpTo(Screen.SongAdded.route) { inclusive = true }
                                    }
                                }else{
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Sin conexión a internet")
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenWidth * 0.04f)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FB3A4))
                    ) {
                        Text(
                            text = "Ir a la reseña",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = screenHeight * 0.02f)
                        )
                    }
                }
            }
        }
    }else{
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}
