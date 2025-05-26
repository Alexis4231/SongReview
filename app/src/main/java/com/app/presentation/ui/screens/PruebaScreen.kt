package com.app.presentation.ui.screens

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.app.R
import android.Manifest
import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.app.MainActivity

@Composable
fun PruebaScreen(){
    val activity = LocalActivity.current
    val modifier = Modifier
        .padding(18.dp)
        .fillMaxWidth()

    activity?.let { SolicitarPermisoNotificaciones(it) }
    LaunchedEffect(Unit){
        activity?.let { createNotificationChannel(it) }
    }

    Column(
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center,
       modifier = Modifier
           .padding(18.dp)
           .fillMaxSize()
    ) {
        Text(
            text = "Notification",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 100.dp)
        )
        Button(
            onClick = { activity?.let { mostrarNotificacion(it) } },
            modifier = Modifier
        ){
            Text("Notificacion simple")
        }
    }
}

fun createNotificationChannel(context: Context){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(
            "mi_canal_id",
            "Mi canal de notificacion",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Descripcion del canal"
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
    }
}

fun mostrarNotificacion(context: Context){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        if(ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
            ){
            Toast.makeText(context,"Permiso no otorgado",return)
        }
    }

    val builder = NotificationCompat.Builder(context,"mi_canal_id")
        .setSmallIcon(R.drawable.logo)
        .setContentTitle("Hola")
        .setContentText("Esto es una prueba")
        .setPriority(Notification.PRIORITY_DEFAULT)
    NotificationManagerCompat.from(context).notify(1,builder.build())
}

@Composable
fun SolicitarPermisoNotificaciones(activity: Activity){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if(!granted){
                Toast.makeText(activity,"Permiso Denegado",Toast.LENGTH_SHORT).show()
            }
        }
    )
    LaunchedEffect(Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}




