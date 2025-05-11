package com.app.presentation.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.presentation.viewmodel.DeezerAlbumViewModel
import com.app.presentation.viewmodel.DeezerGenreViewModel

@Composable
fun SearchScreen(deezerAlbumViewModel: DeezerAlbumViewModel = viewModel(), deezerGenreViewModel: DeezerGenreViewModel = viewModel()) {
    val album = deezerAlbumViewModel.album.collectAsState()
    val genre = deezerGenreViewModel.genre.collectAsState()

    LaunchedEffect(Unit) {
        deezerAlbumViewModel.loadAlbum(7194)
    }


    if(album != null && genre != null){
        Text(album.value?.title + " - " + genre.value?.name)
    }
}
