package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.R
import com.app.presentation.viewmodel.ReviewViewModel
import com.app.presentation.viewmodel.SongDBViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.domain.model.PublicReview
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.DeezerSongViewModel
import com.app.presentation.viewmodel.SpotifyLinkViewModel
import com.app.presentation.viewmodel.SpotifyTokenViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.app.presentation.viewmodel.YoutubeLinkViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ReviewsScreen(navController: NavController, code: String?, songDBViewModel: SongDBViewModel = koinViewModel(), reviewViewModel: ReviewViewModel = koinViewModel(), userViewModel: UserViewModel = koinViewModel() ) {
    val song by songDBViewModel.song.collectAsState()
    val reviews by reviewViewModel.publicReviews.collectAsState()
    var snackbarHostState = remember { SnackbarHostState() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        code?.let { songDBViewModel.getSongByCode(it) }
    }

    LaunchedEffect(Unit) {
        code?.let { reviewViewModel.getReviewByCodeSong(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F))
    ){
        snackbarHostState = SongHeader(song.title)
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF585D5F)),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                code?.let {
                    val rating = if(reviews.isNotEmpty()) {
                        reviews.map { it.rating }.average().roundToInt()
                    }else {
                        0
                    }
                    SongCard(song.title, song.artist, song.genre, rating)
                    CardPublishReview(it, snackbarHostState, navController)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 10.dp)
                ) {
                    Text(text = "Reseñas", fontSize = 30.sp, color = Color.White)
                }
            }

            items(reviews) { review ->
                CardReviews(review)
            }
        }
    }
}

@Composable
fun SongHeader(title: String): SnackbarHostState{
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.fillMaxWidth()
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(
            text= title,
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
    }
    return snackbarHostState
}

@Composable
fun SongCard(
    title: String,
    artist: String,
    genre: String,
    rating: Int,
    deezerSongViewModel: DeezerSongViewModel = viewModel(),
    spotifyLinkViewModel: SpotifyLinkViewModel = viewModel(),
    spotifyTokenViewModel: SpotifyTokenViewModel = viewModel(),
    youtubeLinkViewModel: YoutubeLinkViewModel = viewModel()
){

    val deezerSong = deezerSongViewModel.song.collectAsState()
    val spotifyLink by spotifyLinkViewModel.spotifyLink.collectAsState()
    val spotifyToken = spotifyTokenViewModel.token.collectAsState()
    val youtubeLink by youtubeLinkViewModel.youtubeLink.collectAsState()

    LaunchedEffect(Unit) {
        spotifyTokenViewModel.loadToken()
    }

    LaunchedEffect(artist) {
        deezerSongViewModel.loadSong("""track:"$title" artist:"$artist"""")
        youtubeLinkViewModel.loadLink("$artist  $title")
    }

    LaunchedEffect(spotifyToken.value) {
        spotifyToken.value?.let { spotifyLinkViewModel.loadLink(it.access_token,""""track:"$title" artist:"$artist"""") }
    }

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
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
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
                spotifyLink?.let { MusicPlatformButton(R.drawable.spotify , "Spotify", it.spotify) }
                deezerSong.value?.let { MusicPlatformButton(R.drawable.deezer , "Deezer", it.link) }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                MusicPlatformButton(R.drawable.amazon , "Amazon\nMusic", "https://music.amazon.com/search/"+artist.replace(" ","+")+"+"+title.replace(" ","+"))
                youtubeLink?.let { MusicPlatformButton(R.drawable.youtube , "Youtube", "https://www.youtube.com/watch?v="+it.videoId) }
            }
        }
    }
}

@Composable
fun MusicPlatformButton(@DrawableRes iconRes: Int, name: String, link: String){
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
        },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier
            .widthIn(min = 140.dp)
            .height(56.dp)
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
fun CardPublishReview(
    code: String,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    reviewViewModel: ReviewViewModel = koinViewModel(),
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
){
    LaunchedEffect(Unit) {
        getUserDetailsViewModel.loadUserData()
    }
    val scope = rememberCoroutineScope()
    var textReview by remember { mutableStateOf(TextFieldValue("")) }
    var punctuation by remember { mutableStateOf(0) }
    val user = getUserDetailsViewModel.user.value
    var showDialog by remember { mutableStateOf(false) }

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
                    if(textReview.text.isEmpty()){
                        scope.launch {
                            snackbarHostState.showSnackbar("Introduce un comentario")
                        }
                    }else if(punctuation == 0){
                        scope.launch {
                            snackbarHostState.showSnackbar("Introduce una puntuación")
                        }
                    }else {
                        if (user != null) {
                            reviewViewModel.setCodeUser(user.code)
                            val publicReview = PublicReview(codeSong = code, rating = punctuation, comment = textReview.text, username = user.name)
                            reviewViewModel.setPublicReview(publicReview)
                            reviewViewModel.save()
                            textReview = TextFieldValue("")
                            punctuation = 0
                            navController.navigate(Screen.Reviews.createRoute(code)) {
                                popUpTo(Screen.Reviews.createRoute(code)) { inclusive = true }
                            }
                        } else {
                            showDialog = true
                        }
                    }
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
        showErrorPopup(showError = showDialog, message = "Usuario no encontrado") {
            showDialog = false
        }
    }
}

@Composable
fun CardReviews(review: PublicReview){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF44A898))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = review.username.toUpperCase().firstOrNull()?.toString() ?: "",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = review.username, fontWeight = FontWeight.SemiBold, color = Color.White)
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

@Composable
fun showErrorPopup(
    showError: Boolean,
    message: String,
    onClose: () -> Unit
) {
    if(showError){
        AlertDialog(
            onDismissRequest = onClose,
            title = { Text("Error") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onClose) {
                    Text("Vale")
                }
            }
        )
    }
}