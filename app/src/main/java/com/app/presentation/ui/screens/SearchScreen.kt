package com.app.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.presentation.viewmodel.ArtistViewModel
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    val viewModel: ArtistViewModel = viewModel()
    val artists = viewModel.artists.value

    LaunchedEffect(Unit) {
        viewModel.searchArtist("a")
    }

    Scaffold(
        topBar = {
            TopAppBar(title= { Text("Artistas de Spotify") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(artists) { artist ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ){
                    Text(artist.name)
                }
            }
        }
    }
}