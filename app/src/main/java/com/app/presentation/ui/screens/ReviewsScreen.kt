import GetUserDetailsViewModel
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.ExoPlayer
import com.app.domain.model.PublicReview
import com.app.isInternetAvailable
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.DeezerSongViewModel
import com.app.presentation.viewmodel.SpotifyLinkViewModel
import com.app.presentation.viewmodel.SpotifyTokenViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.app.presentation.viewmodel.YoutubeLinkViewModel
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt
import androidx.media3.common.MediaItem
import kotlinx.coroutines.delay

@Composable
fun ReviewsScreen(
    navController: NavController,
    code: String?,
    songDBViewModel: SongDBViewModel = koinViewModel(),
    reviewViewModel: ReviewViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    val song by songDBViewModel.song.collectAsState()
    val reviews by reviewViewModel.publicReviews.collectAsState()
    val scope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val density = LocalDensity.current.density
    val loadingReviews by reviewViewModel.isLoading.collectAsState()


    LaunchedEffect(Unit) {
        code?.let { songDBViewModel.getSongByCode(it) }
        code?.let { reviewViewModel.getReviewByCodeSong(it) }
    }

    val isLoading = song.title.isEmpty()

    if(isInternetAvailable(context)) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF585D5F)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .zIndex(1f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF585D5F))
                ) {
                    snackbarHostState = SongHeader(song.title)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF585D5F)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            code?.let {
                                val rating = if (reviews.isNotEmpty()) {
                                    reviews.map { it.rating }.average().roundToInt()
                                } else {
                                    0
                                }
                                SongCard(song.title, song.artist, song.genre, rating)
                                CardPublishReview(it, snackbarHostState, scope, navController)
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
                        if (loadingReviews) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color(0xFF585D5F)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                }
                            }
                        } else {
                            if (reviews.isNotEmpty()) {
                                items(reviews) { review ->
                                    CardReviews(review, navController, snackbarHostState, scope)
                                }
                            } else {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Mood,
                                                contentDescription = "No reviews",
                                                tint = Color.White,
                                                modifier = Modifier.size(64.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Añade el primer comentario",
                                                color = Color.White,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }else{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.SignalWifiOff,
                    contentDescription = "SignalWifiOff",
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Sin conexión a internet",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun SongHeader(title: String): SnackbarHostState{
    val snackbarHostState = remember { SnackbarHostState() }

    Box{
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .zIndex(1f)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = title,
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
    return snackbarHostState
}

@SuppressLint("SuspiciousIndentation")
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
        youtubeLinkViewModel.loadLink("$artist $title")
    }

    LaunchedEffect(spotifyToken.value) {
        val token = spotifyToken.value
        if(token != null){
            spotifyLinkViewModel.loadLink(token.access_token, """track:"$title" artist:"$artist"""")
        }
    }

    val isDeezerLoaded = deezerSong.value != null
    val isSpotifyLoaded = spotifyLink != null
    val isYoutubeLoaded = youtubeLink != null
    val allLinksLoaded = isDeezerLoaded && isSpotifyLoaded && isYoutubeLoaded

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF39D0B9)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Titulo: ${title}",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
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
                ) {
                    Text(text = "Artista: $artist", color = Color.White)
                    Text(text = "Género: $genre", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
                if(allLinksLoaded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        spotifyLink?.let {
                            MusicPlatformButton(
                                R.drawable.spotify,
                                "Spotify",
                                it.spotify
                            )
                        }
                        deezerSong.value?.let {
                            MusicPlatformButton(
                                R.drawable.deezer,
                                "Deezer",
                                it.link
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MusicPlatformButton(
                            R.drawable.amazon,
                            "Amazon\nMusic",
                            "https://music.amazon.com/search/" + artist.replace(
                                " ",
                                "+"
                            ) + "+" + title.replace(" ", "+")
                        )
                        youtubeLink?.let {
                            deezerSong.value?.let { it1 ->
                                MusicPlatformButton(
                                    R.drawable.youtube,
                                    "Youtube",
                                    "https://www.youtube.com/watch?v=" + it.videoId
                                )
                            }
                        }
                    }
                        deezerSong.value?.preview?.let{previewUrl ->
                            Spacer(modifier = Modifier.height(16.dp))
                            PreviewPlayer(previewUrl)
                    }
                }else{
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF39D0B9)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
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
    scope: CoroutineScope,
    navController: NavController,
    reviewViewModel: ReviewViewModel = koinViewModel(),
    getUserDetailsViewModel: GetUserDetailsViewModel = koinViewModel(),
    context: Context = LocalContext.current
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
                    if(isInternetAvailable(context)) {
                        if (textReview.text.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Introduce un comentario")
                            }
                        } else if (punctuation == 0) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Introduce una puntuación")
                            }
                        } else {
                            if (user != null) {
                                reviewViewModel.setCodeUser(user.code)
                                val publicReview = PublicReview(
                                    codeSong = code,
                                    rating = punctuation,
                                    comment = textReview.text,
                                    username = user.name
                                )
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
                    }else{
                        scope.launch {
                            snackbarHostState.showSnackbar("Sin conexión a internet")
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
fun CardReviews(
    review: PublicReview,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context = LocalContext.current
){
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable {
                        if(isInternetAvailable(context)){
                            navController.navigate(Screen.CardUser.createRoute(review.username))
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar("Sin conexión a internet")
                            }
                        }
                    }
                ) {
                    Text(
                        text = review.username.toUpperCase().firstOrNull()?.toString() ?: "",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.username,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.clickable {
                        if(isInternetAvailable(context)){
                        navController.navigate(Screen.CardUser.createRoute(review.username))
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar("Sin conexión a internet")
                            }
                        }
                    }
                )
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

@Composable
fun PreviewPlayer(previewUrl: String){
    val context = LocalContext.current
    val exoPlayer = remember{
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(previewUrl))
            prepare()
        }
    }

    var isPlaying by remember { mutableStateOf(false) }
    var playbackPosition by remember {mutableStateOf(0L)}
    var duration by remember {mutableStateOf(0L)}

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(isPlaying){
        while(isPlaying){
            playbackPosition = exoPlayer.currentPosition
            duration = exoPlayer.duration
            delay(500)
        }
    }

    Column(modifier = Modifier.padding(16.dp)){
        IconButton(
            onClick = {
                if(isPlaying){
                    exoPlayer.pause()
                }else{
                    exoPlayer.play()
                }
                isPlaying = !isPlaying
            },
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
        ){
            Icon(
                imageVector = if(isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if(isPlaying) "Pause" else "Start",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }
        Slider(
            value = playbackPosition.coerceAtLeast(0L).toFloat(),
            onValueChange = {
                playbackPosition = it.toLong()
                exoPlayer.seekTo(playbackPosition)
            },
            valueRange = 0f..(duration.coerceAtLeast(1L).toFloat()),
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}