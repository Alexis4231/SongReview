package com.app.presentation.ui.screens

import GetUserDetailsViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.domain.model.Genre
import com.app.presentation.navigation.Screen
import com.app.presentation.viewmodel.DeezerGenresViewModel
import com.app.presentation.viewmodel.SongDBViewModel
import com.app.presentation.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController) {
    var selectedProfileItem by remember { mutableStateOf(0) }
    val profileItems = listOf("Canciones", "Artistas","Usuarios")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp,0.dp,0.dp)
            .background(Color(0xFF585D5F)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedProfileItem == 0) {
            HomeHeader(true)
        } else {
            HomeHeader(false)
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
            0 -> listSongsContent(navController)
            1 -> listArtistsContent()
            2 -> listProfilesContent(navController)
        }
    }
}

@Composable
fun HomeHeader(showStyles: Boolean, deezerGenresViewModel: DeezerGenresViewModel = viewModel()){
    var search by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val genres by deezerGenresViewModel.genres.collectAsState()
    var selectedStyle by remember { mutableStateOf("Todos") }
    val allGenres = listOf(Genre(0,"Todos")) + genres

    LaunchedEffect(Unit) {
        if (genres.isEmpty()) {
            deezerGenresViewModel.loadGenres()
        }
        selectedStyle = "Todos"
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
                onValueChange = { search = it },
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
                                    selectedStyle = genre.name
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
fun listSongsContent(navController: NavController, songDBViewModel: SongDBViewModel = koinViewModel()){
    val songs by songDBViewModel.songs.collectAsState()

    LaunchedEffect(Unit) {
        songDBViewModel.getSongs()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(songs) { song ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                onClick = { navController.navigate(Screen.Reviews.createRoute(song.code)) }
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

@Composable
fun listArtistsContent(){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(50) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Text(
                    text = "Artista #$index",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
fun listProfilesContent(navController: NavController, userViewModel: UserViewModel = koinViewModel(), userDetailsUserViewModel: GetUserDetailsViewModel = viewModel()){
    val users by userViewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        userDetailsUserViewModel.loadUserData()
    }

    val myUser by userDetailsUserViewModel.user

    LaunchedEffect(Unit) {
        userViewModel.getUsers()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(users) { user ->
            if (user.name != myUser?.name) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { }
                ) {
                    Text(
                        text = user.name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}