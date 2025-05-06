package com.app.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.presentation.viewmodel.ArtistViewModel
import com.app.presentation.viewmodel.SpotifyViewModel

@Composable
fun SearchScreen(artistViewModel: ArtistViewModel = viewModel(), spotifyViewModel: SpotifyViewModel = viewModel()) {
    val artist by artistViewModel.artist.collectAsState()
    val token by spotifyViewModel.token.collectAsState()

    LaunchedEffect(token) {
        if(token == null) {
            spotifyViewModel.loadToken()
        }else{
            token?.let { artistViewModel.loadArtist(it.access_token) }
        }
    }
        Text(artist?.name + " tiene " + artist?.followers?.total + " seguidores")
}