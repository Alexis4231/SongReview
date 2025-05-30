package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.domain.model.Genre
import com.app.isInternetAvailable
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.DeezerGenresViewModel
import com.app.presentation.viewmodel.SongDBViewModel
import com.app.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController) {
    var selectedProfileItem by remember { mutableStateOf(0) }
    var search by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Todos") }
    val profileItems = listOf("Canciones", "Artistas","Usuarios")
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .zIndex(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp)
                .background(Color(0xFF585D5F)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedProfileItem == 0) {
                HomeHeader(
                    true,
                    search,
                    onSearchChange = { search = it },
                    selectedStyle,
                    onSelectedStyleChange = { selectedStyle = it })
            } else {
                HomeHeader(
                    false,
                    search,
                    onSearchChange = { search = it },
                    selectedStyle,
                    onSelectedStyleChange = { selectedStyle = it })
            }
            NavigationBar(containerColor = Color(0xFF44A898)) {
                profileItems.forEachIndexed { index, item ->
                    NavigationBar(containerColor = Color(0xFF4FB3A4)) {
                        profileItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = item,
                                            color = Color.White
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .height(2.dp)
                                                .width(24.dp)
                                                .background(
                                                    if (selectedProfileItem == index) Color.White else Color.Transparent
                                                )
                                        )
                                    }
                                },
                                selected = selectedProfileItem == index,
                                onClick = { selectedProfileItem = index },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent,
                                    selectedIconColor = Color.White,
                                    unselectedIconColor = Color.LightGray,
                                    selectedTextColor = Color.White,
                                    unselectedTextColor = Color.LightGray
                                ),
                                modifier = Modifier
                                    .background(Color(0xFF4FB3A4))
                            )
                        }
                    }
                }
            }

            when (selectedProfileItem) {
                0 -> listSongsContent(
                    navController,
                    search,
                    selectedStyle,
                    snackbarHostState,
                    scope
                )

                1 -> listArtistsContent(navController, search, snackbarHostState, scope)
                2 -> listProfilesContent(navController, search, snackbarHostState, scope)
            }
        }
    }
}

@Composable
fun HomeHeader(
    showStyles: Boolean,
    search: String,
    onSearchChange: (String) -> Unit,
    selectedStyle: String,
    onSelectedStyleChange: (String) -> Unit,
    deezerGenresViewModel: DeezerGenresViewModel = viewModel()){
    var expanded by remember { mutableStateOf(false) }
    val genres by deezerGenresViewModel.genres.collectAsState()
    val allGenres = listOf(Genre(0,"Todos")) + genres

    LaunchedEffect(Unit) {
        if (genres.isEmpty()) {
            deezerGenresViewModel.loadGenres()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(0xFF39D0B9),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = search,
                onValueChange = onSearchChange,
                placeholder = { Text("Buscar", color = Color.White) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF39D0B9),
                    unfocusedContainerColor = Color(0xFF39D0B9),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                singleLine = true
            )

            if (showStyles) {
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                        .background(Color.White)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.height(56.dp),
                        border = BorderStroke(0.dp, Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Género"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = selectedStyle)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        allGenres.forEach { genre ->
                            DropdownMenuItem(
                                text = { Text(genre.name) },
                                onClick = {
                                    onSelectedStyleChange(genre.name)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun listSongsContent(
    navController: NavController,
    search: String,
    selectedStyle: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    songDBViewModel: SongDBViewModel = koinViewModel(),
    context: Context = LocalContext.current
){
    val songs by songDBViewModel.songs.collectAsState()
    val isLoading by songDBViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit){
        songDBViewModel.getSongs()
    }

    val filteredSongs = songs.filter {song ->
        song.title.contains(search, ignoreCase = true) && (selectedStyle == "Todos" || song.genre.equals(selectedStyle, ignoreCase = true))
    }

    if(isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF585D5F)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }else{
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredSongs) { song ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = {
                        if(isInternetAvailable(context)) {
                            navController.navigate(Screen.Reviews.createRoute(song.code))
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar("Sin conexión a internet")
                            }
                        }
                    }
                ) {
                    Text(
                        text = song.title + " - " + song.artist + " (" + song.genre + ")",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun listArtistsContent(
    navController: NavController,
    search: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    songDBViewModel: SongDBViewModel = koinViewModel(),
    context: Context = LocalContext.current
){
        val density = LocalDensity.current.density
        val songs by songDBViewModel.songs.collectAsState()

        LaunchedEffect(Unit) {
            songDBViewModel.getSongs()
        }

        val filteredArtist = if(search.length >= 1){
            songs.filter {
                it.artist.contains(search, ignoreCase = true)
            }
        }else{
            emptyList()
        }

        if(search.length < 1) {
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
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Busca un artista",
                        color = Color.White,
                        fontSize = (10 * density).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize()

            ) {
                items(filteredArtist) { song ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        onClick = {
                            if(isInternetAvailable(context)) {
                                navController.navigate(Screen.Reviews.createRoute(song.code))
                            }else{
                                scope.launch {
                                    snackbarHostState.showSnackbar("Sin conexión a internet")
                                }
                            }
                        }
                    ) {
                        Text(
                            text = song.artist + " - " + song.title,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
}


@Composable
fun listProfilesContent(
    navController: NavController,
    search: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    userViewModel: UserViewModel = koinViewModel(),
    userDetailsUserViewModel: GetUserDetailsViewModel = viewModel(),
    context: Context = LocalContext.current
){
    val density = LocalDensity.current.density
    val usernames by userViewModel.names.collectAsState()

    LaunchedEffect(Unit) {
        userDetailsUserViewModel.loadUserData()
    }

    val myUser by userDetailsUserViewModel.user

    LaunchedEffect(Unit) {
        userViewModel.getUsers()
    }

    val filteredProfiles = if(search.length >= 1){
        usernames.filter {
            it.contains(search, ignoreCase = true)
        }
    }else{
        emptyList()
    }

    if(search.length < 1){
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
                    imageVector = Icons.Default.Mood,
                    contentDescription = "Mood",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Busca un perfil",
                    color = Color.White,
                    fontSize = (10 * density).sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredProfiles) { username ->
                if (username != myUser?.name) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF44A898)),
                        onClick = {
                            if(isInternetAvailable(context)) {
                                navController.navigate(Screen.CardUser.createRoute(username))
                            }else{
                                scope.launch {
                                    snackbarHostState.showSnackbar("Sin conexión a internet")
                                }
                            }
                        }
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
                                ) {
                                    Text(
                                        text = username.toUpperCase().firstOrNull()?.toString() ?: "",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = username,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}