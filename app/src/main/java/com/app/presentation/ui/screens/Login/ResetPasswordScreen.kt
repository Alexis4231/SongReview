package com.app.presentation.ui.screens.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.R
import com.app.presentation.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(navController: NavController){
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.LockReset,
                contentDescription = "ResetPassword",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = screenWidth*0.07f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Introduce tu correo electrónico",
                color = Color.White,
                fontSize = (8.5*density).sp,
                fontWeight = FontWeight.Bold
            )
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                cursorBrush = SolidColor(Color.White),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .background(Color.Black)
                    .padding(horizontal = screenWidth * 0.07f)
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color.White,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Outlined.Mail, contentDescription = "Mail",tint = Color.White, modifier = Modifier.padding(0.dp,0.dp,screenWidth*0.05f,0.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (email.isEmpty()) {
                                Text("Correo electrónico", color = Color.White)
                            }
                            innerTextField()
                        }
                        IconButton(onClick = { email = "" }) {
                            Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Cancel", tint = Color.White)
                        }
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth()
                .padding(horizontal = screenWidth * 0.07f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                enabled = !isLoading,
                onClick = {
                    if (email.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Introduce tu correo electrónico")
                        }
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        scope.launch {
                            isLoading = false
                            snackbarHostState.showSnackbar("Debes introducir un correo electrónico")
                        }
                    }else {
                        scope.launch {
                            resetpassword(email)
                            navController.navigate(Screen.SuccessResetPassword.route)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth * 0.04f)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF585D5F))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        text = "Reestablecer contraseña",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = screenHeight * 0.02f)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = screenWidth * 0.07f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.ResetPassword.route) { inclusive = true }
                    }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth * 0.04f)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF585D5F))
            ) {
                Text(
                    text = "Volver",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = screenHeight * 0.02f)
                )
            }
        }
    }
}

fun resetpassword(email: String){
    Firebase.auth.sendPasswordResetEmail(email)
}