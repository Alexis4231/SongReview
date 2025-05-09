package com.app.presentation.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.presentation.viewmodel.DeezerAlbumsViewModel

@Composable
fun AlbumsScreenByArtist(
    artistId: Int,
    viewModel: DeezerAlbumsViewModel = viewModel()
) {
    val albums by viewModel.albums.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadAlbums(artistId)
    }

    LazyColumn {
        items(albums) { album ->

        }
    }
}
