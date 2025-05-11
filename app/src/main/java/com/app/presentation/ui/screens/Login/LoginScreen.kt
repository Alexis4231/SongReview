package com.app.presentation.ui.screens.Login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.R
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel = koinViewModel()) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var userOrMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    var isLoading by remember { mutableStateOf(false) }
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
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = screenWidth*0.07f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            BasicTextField(
                value = userOrMail,
                onValueChange = { userOrMail = it },
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
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = "Person",tint = Color.White, modifier = Modifier.padding(0.dp,0.dp,screenWidth*0.05f,0.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (userOrMail.isEmpty()) {
                                Text("Usuario o correo", color = Color.White)
                            }
                            innerTextField()
                        }
                        IconButton(onClick = { userOrMail = "" }) {
                            Icon(imageVector = Icons.Outlined.Cancel, contentDescription = "Cancel", tint = Color.White)
                        }
                    }
                }
            )
            BasicTextField(
                value = password,
                onValueChange = { password = it},
                singleLine = true,
                cursorBrush = SolidColor(Color.White),
                visualTransformation = PasswordVisualTransformation(),
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
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Pass",tint = Color.White, modifier = Modifier.padding(0.dp,0.dp,screenWidth*0.05f,0.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (password.isEmpty()) {
                                    Text("Contraseña", color = Color.White)
                                }
                                innerTextField()

                            }
                            IconButton(onClick = { password = "" }) {
                                Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null, tint = Color.White)
                            }
                        }
                    }
                }
            )
            Text(
                text="¿Olvidaste tu contraseña?",
                color = Color(0xFF39D0B9),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(0.dp,0.dp,screenWidth*0.05f,0.dp)
                    .clickable {
                    navController.navigate(Screen.ResetPassword.route)
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
                        if (userOrMail.isEmpty() || password.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Completa todos los campos")
                            }
                        } else {
                            isLoading = true
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userOrMail).matches()){
                                userViewModel.getEmail(userOrMail) { email ->
                                    if (email != null) {
                                        loginUser(email, password) { success, errorMessage ->
                                            scope.launch {
                                                if (success) {
                                                    navController.navigate(Screen.Home.route)
                                                } else {
                                                    isLoading = false
                                                    snackbarHostState.showSnackbar(errorMessage.toString())
                                                }
                                            }
                                        }
                                    } else {
                                        scope.launch {
                                            isLoading = false
                                            snackbarHostState.showSnackbar("Usuario o contraseña incorrectos")
                                        }
                                    }
                                }
                            } else {
                                loginUser(userOrMail, password) { success, errorMessage ->
                                    scope.launch {
                                        if (success) {
                                            navController.navigate(Screen.Home.route)
                                        } else {
                                            isLoading = false
                                            snackbarHostState.showSnackbar(errorMessage.toString())
                                        }
                                    }
                                }
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
                            text = "Iniciar sesión",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = screenHeight * 0.02f)
                        )
                    }
            }
        }
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Registrarse",
                color = Color(0xFF39D0B9),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun loginUser(email: String, password: String, onComplete:(Boolean, String?) -> Unit){
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null && user.isEmailVerified) {
                    onComplete(true, null)
                } else {
                    auth.signOut()
                    onComplete(false, "Verifica tu correo electrónico para iniciar sesión")
                }
            } else {
                onComplete(false, "Correo o contraseña incorrectos")
            }
        }
}