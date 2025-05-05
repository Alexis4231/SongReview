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
        val token = "BQCEVP63-XnaEBSS0-ZYaltkjvsFyPwIWvuJs8lRCENQRjWuvnaIHcfBL-dx0vvxaF0ehp4MXYtoBK4P0D66iyEkHKpijyCCa_ug_mnRkUh88dWJONXG2N_GS7enzc76KlnkwWoK7Ds"
        viewModel.loadArtist(token)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Artista")
        Text(artist?.name + " tiene " + artist?.followers?.total + " seguidores")
    }
}