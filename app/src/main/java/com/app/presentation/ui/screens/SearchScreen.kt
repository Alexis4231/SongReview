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

@Composable
fun SearchScreen(viewModel: ArtistViewModel = viewModel()) {
    val artist by viewModel.artist.collectAsState()

    LaunchedEffect(Unit) {
        val token = "BQD-ZKxYD-JyPAoWhYufLcogv3LbfIgtNsS0DQHbzG2_5VIb_Lh4YSNx4ZVmmObgb4fGg27I9ayRHDcw4Uu7-0M9AjAX_pCFE5EoHWLgWiyyq-tjD--KCfFeckDfDWltR5BrzvaiyM4"
        viewModel.loadArtist(token)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Artista")
        Text(artist?.name + " tiene " + artist?.followers?.total + " seguidores")
    }
}