package com.app.presentation.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.presentation.viewmodel.DeezerAlbumsViewModel
import com.app.presentation.viewmodel.DeezerArtistsViewModel
import com.app.presentation.viewmodel.GenresViewModel
import com.app.presentation.viewmodel.SongsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(genresViewModel: GenresViewModel = viewModel(), deezerArtistsViewModel: DeezerArtistsViewModel = viewModel(), songsViewModel: SongsViewModel = viewModel(), albumsViewModel: DeezerAlbumsViewModel = viewModel(), onSearchArtist: (String) -> Unit = {}, onSearchSong: (String) -> Unit = {}) {
    var song by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    val genres by genresViewModel.genres.collectAsState()
    var selectedStyle by remember { mutableStateOf("Seleccionar estilo") }
    var expanded by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val rotationAngle by animateFloatAsState(if (expanded) 180f else 0f)
    var songSelectedState by remember { mutableStateOf(false) }
    var artistSelectedState by remember { mutableStateOf(false) }
    val suggestionsArtists by deezerArtistsViewModel.artists.collectAsState()
    val suggestionsSongs by songsViewModel.songs.collectAsState()
    var isSuggestionArtistsVisible by remember { mutableStateOf(false) }
    var isSuggestionSongsVisible by remember { mutableStateOf(false) }
    var songTextFieldPositionY by remember { mutableStateOf(0f) }
    var songTextFieldHeight by remember { mutableStateOf(0f) }
    var artistTextFieldPositionY by remember { mutableStateOf(0f) }
    var artistTextFieldHeight by remember { mutableStateOf(0f) }
    val albums by albumsViewModel.albums.collectAsState()
    var currentPageArtist by remember { mutableStateOf(1) }
    var currentPageSongs by remember { mutableStateOf(1) }

    LaunchedEffect(artist) {
        if (artist.isNotBlank()) {
            deezerArtistsViewModel.loadArtists(artist, currentPageArtist)
            isSuggestionArtistsVisible = true
        } else {
            isSuggestionArtistsVisible = false
        }
    }

    LaunchedEffect(song) {
        if (song.isNotBlank()) {
            songsViewModel.loadSongs(song, currentPageSongs)
            isSuggestionSongsVisible = true
        } else {
            isSuggestionSongsVisible = false
        }
    }

    LaunchedEffect(Unit) {
        if (genres.isEmpty()) {
            genresViewModel.loadGenres()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF39D0B9)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Añadir nueva canción",
                fontSize = 30.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .padding(horizontal = screenWidth * 0.07f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Canción",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(top = 12.dp)
                )
                if(!songSelectedState) {
                    Box(modifier = Modifier.weight(0.7f)) {
                        Column {
                            BasicTextField(
                                value = song,
                                onValueChange = {
                                    song = it
                                },
                                singleLine = true,
                                cursorBrush = SolidColor(Color.White),
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                    .onGloballyPositioned { layoutCoordinates ->
                                        songTextFieldPositionY =
                                            layoutCoordinates.positionInRoot().y
                                        songTextFieldHeight =
                                            layoutCoordinates.size.height.toFloat()
                                    },
                                decorationBox = { innerTextField ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Search,
                                            contentDescription = "Search",
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Box(modifier = Modifier.weight(1f)) {
                                            if (song.isEmpty()) {
                                                Text("Buscar Canción", color = Color.White)
                                            }
                                            innerTextField()
                                        }
                                        IconButton(onClick = {
                                            song = ""
                                            isSuggestionSongsVisible = false
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Cancel,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            )

                            if (song.isNotBlank() && isSuggestionSongsVisible && suggestionsSongs.isNotEmpty()) {
                                val filtered = suggestionsSongs
                                    .filter { it.title.contains(song, ignoreCase = true) }
                                val density = LocalDensity.current
                                val offsetY = with(density) { songTextFieldHeight.toDp() }

                                Popup(
                                    alignment = Alignment.TopStart,
                                    offset = with(density) {
                                        IntOffset(
                                            x = 0,
                                            y = offsetY.roundToPx()
                                        )
                                    },
                                    properties = PopupProperties(focusable = false)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .zIndex(1f)
                                            .heightIn(max = 150.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        LazyColumn {
                                            items(filtered) { suggestion ->
                                                Text(
                                                    text = suggestion.title + " (" + suggestion.artist.name + ")",
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            song = suggestion.title
                                                            isSuggestionSongsVisible = false
                                                            onSearchSong(suggestion.title)
                                                            songSelectedState = true
                                                            artist = suggestion.artist.name
                                                            artistSelectedState = true
                                                        }
                                                        .padding(12.dp),
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(Color(0xFF39D0B9))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = song,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            IconButton(onClick = {
                                songSelectedState = false
                                song = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Cancel,
                                    contentDescription = "Cancel",
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Artista",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(0.3f)
                )
                if (!artistSelectedState) {
                    Box(modifier = Modifier.weight(0.7f)) {
                        Column {
                            BasicTextField(
                                value = artist,
                                onValueChange = {
                                    artist = it
                                },
                                singleLine = true,
                                cursorBrush = SolidColor(Color.White),
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                    .onGloballyPositioned { layoutCoordinates ->
                                        artistTextFieldPositionY =
                                            layoutCoordinates.positionInRoot().y
                                        artistTextFieldHeight =
                                            layoutCoordinates.size.height.toFloat()
                                    },
                                decorationBox = { innerTextField ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Search,
                                            contentDescription = "Search",
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Box(modifier = Modifier.weight(1f)) {
                                            if (artist.isEmpty()) {
                                                Text("Buscar artista", color = Color.White)
                                            }
                                            innerTextField()
                                        }
                                        IconButton(onClick = {
                                            artist = ""
                                            isSuggestionArtistsVisible = false
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Cancel,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            )

                            if (artist.isNotBlank() && isSuggestionArtistsVisible && suggestionsArtists.isNotEmpty()) {
                                val filtered = suggestionsArtists
                                    .filter { it.name.contains(artist, ignoreCase = true) }
                                    .distinctBy { it.name }
                                val density = LocalDensity.current
                                val offsetY = with(density) { artistTextFieldHeight.toDp() }

                                Popup(
                                    alignment = Alignment.TopStart,
                                    offset = with(density) {
                                        IntOffset(
                                            x = 0,
                                            y = offsetY.roundToPx()
                                        )
                                    },
                                    properties = PopupProperties(focusable = false)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .zIndex(1f)
                                            .heightIn(max = 150.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        LazyColumn {
                                            items(filtered) { suggestion ->
                                                Text(
                                                    text = suggestion.name,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            artist = suggestion.name
                                                            isSuggestionArtistsVisible = false
                                                            onSearchArtist(suggestion.name)
                                                            artistSelectedState = true
                                                        }
                                                        .padding(12.dp),
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(Color(0xFF39D0B9))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = artist,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            IconButton(onClick = {
                                artistSelectedState = false
                                artist = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Cancel,
                                    contentDescription = "Cancel",
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Estilo",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(0.3f)
                )
                if(selectedStyle.equals("Seleccionar estilo")){
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.weight(0.7f)
                    ) {
                        TextField(
                            value = selectedStyle,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(
                                Color.White,
                                textAlign = TextAlign.Center
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropUp,
                                    contentDescription = "Dropdown Arrow",
                                    tint = Color.White,
                                    modifier = Modifier.rotate(rotationAngle)
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                focusedContainerColor = Color(0xFF585D5F),
                                unfocusedContainerColor = Color(0xFF585D5F),
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White
                            ),
                            modifier = Modifier
                                .menuAnchor(),

                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genres.forEach { genre ->
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
                }else{
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .background(Color(0xFF39D0B9))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = selectedStyle,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            IconButton(onClick = {selectedStyle = "Seleccionar estilo"}) {
                                Icon(
                                    imageVector = Icons.Outlined.Cancel,
                                    contentDescription = "Cancel",
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .padding(horizontal = screenWidth * 0.07f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth * 0.04f)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39D0B9))
            ) {
                Text(
                    text = "Añadir",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = screenHeight * 0.02f)
                )
            }
        }
    }
}