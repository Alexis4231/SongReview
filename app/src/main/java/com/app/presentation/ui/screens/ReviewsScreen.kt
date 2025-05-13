package com.app.presentation.ui.screens

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.R
import com.app.presentation.viewmodel.SongDBViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReviewsScreen(navController: NavController, code: String?, songDBViewModel: SongDBViewModel = koinViewModel()) {
    val song by songDBViewModel.song.collectAsState()

    LaunchedEffect(Unit) {
        code?.let { songDBViewModel.getSongByCode(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SongHeader(song.title)
        if (code != null) {
            SongCard(song.title,song.artist,song.genre,2)
        }
        CardPublishReview()
    }
}

@Composable
fun SongHeader(title: String){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(text= title, fontSize = 30.sp, color = Color.White)
    }
}

@Composable
fun SongCard(
    title: String,
    artist: String,
    genre: String,
    rating: Int
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF39D0B9)),
    ){
       Column(
           modifier = Modifier.padding(16.dp)
       ){
           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ){
               Text(
                   text = "Titulo: ${title}",
                   color = Color.White,
                   fontSize = 16.sp
               )
               Row{
                   repeat(5){index ->
                       Icon(
                           imageVector = if(index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                           contentDescription = "Rating",
                           tint = Color.White
                       )
                   }
               }
           }

           Spacer(modifier = Modifier.height(8.dp))

           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween
           ){
               Text(text = "Artista: $artist", color = Color.White)
               Text(text="Género: $genre", color = Color.White)
           }
           Spacer(modifier = Modifier.height(16.dp))

           Row(
               modifier = Modifier
                   .fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween
           ){
               MusicPlatformButton(R.drawable.spotify , "Spotify")
               MusicPlatformButton(R.drawable.deezer , "Deezer")
           }

           Spacer(modifier = Modifier.height(8.dp))

           Row(
               modifier = Modifier
                   .fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween
           ){
               MusicPlatformButton(R.drawable.amazon , "Amazon Music")
               MusicPlatformButton(R.drawable.youtube , "Youtube")
           }

       }
    }
}

@Composable
fun MusicPlatformButton(@DrawableRes iconRes: Int, name: String){
    Button(
        onClick = {},
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier
            .widthIn(min = 140.dp)
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = name,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun CardPublishReview(){
    var textReview by remember { mutableStateOf(TextFieldValue("")) }
    var punctuation by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF39D0B9)),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(
                text = "Escribe tu reseña:",
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = textReview,
                onValueChange = { textReview = it},
                placeholder = { Text("Tu opinión sobre la canción...")},
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp),
                maxLines = 5,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu puntuación: ",
                fontSize = 16.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                repeat(5){index ->
                    Icon(
                        imageVector = if(index < punctuation) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Stars",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable { punctuation = index + 1 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    textReview = TextFieldValue("")
                    punctuation = 0
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ){
                Text("Publicar")
            }
        }
    }
}
