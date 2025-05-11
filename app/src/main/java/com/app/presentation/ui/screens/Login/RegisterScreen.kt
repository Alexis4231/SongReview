package com.app.presentation.ui.screens.Login

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
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
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel = koinViewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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
                value = name,
                onValueChange = { name = it },
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
                            if (name.isEmpty()) {
                                Text("Usuario", color = Color.White)
                            }
                            innerTextField()
                        }
                        IconButton(onClick = { name = "" }) {
                            Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null, tint = Color.White)
                        }
                    }
                }
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
                            Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            )
            BasicTextField(
                value = password,
                onValueChange = { password = it },
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
            )

        }
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = screenWidth * 0.07f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                enabled = !isLoading,
                onClick = {
                    if(password.isEmpty() || email.isEmpty() || name.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Completa todos los campos")
                        }
                    }else{
                        isLoading = true
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            scope.launch {
                                isLoading = false
                                snackbarHostState.showSnackbar("Correo electrónico inválido")
                            }
                        } else if (password.length < 6) {
                            scope.launch {
                                isLoading = false
                                snackbarHostState.showSnackbar("La contraseña debe tener al menos 6 carácteres")
                            }
                        } else {
                            userViewModel.avaliableUsername(
                                name,
                                {
                                    registerUser(email, password) { success ->
                                        scope.launch {
                                            if (success) {
                                                userViewModel.setName(name)
                                                userViewModel.setEmail(email)
                                                userViewModel.save()
                                                navController.navigate(Screen.SuccesRegister.route)

                                            } else {
                                                isLoading = false
                                                snackbarHostState.showSnackbar("Error al crear el usuario")
                                            }
                                        }
                                    }
                                },
                                {
                                    scope.launch {
                                        isLoading = false
                                        snackbarHostState.showSnackbar("El nombre de usuario ya existe")
                                    }
                                }
                            )
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
                if(isLoading){
                    CircularProgressIndicator(color = Color.White)
                }else {
                    Text(
                        text = "Registrarse",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = screenHeight * 0.02f)
                    )
                }
            }
        }
    }
}

fun registerUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                    if (verifyTask.isSuccessful) {
                        auth.signOut()
                        onComplete(true)
                    } else {
                        onComplete(false)
                    }
                }
            } else {
                onComplete(false)
            }
        }
}