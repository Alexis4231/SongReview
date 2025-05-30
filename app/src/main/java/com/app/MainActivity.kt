package com.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.app.presentation.navigation.NavGraph
import com.app.presentation.ui.theme.SongReviewTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username")
        val receiverUid = intent.getStringExtra("receiverUid")
        enableEdgeToEdge()
        setContent {
            SongReviewTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    username = username,
                    receiverUid = receiverUid
                )
            }
        }
    }
}