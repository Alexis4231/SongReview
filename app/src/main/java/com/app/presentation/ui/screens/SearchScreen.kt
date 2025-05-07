package com.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.presentation.viewmodel.ArtistsViewModel
import com.app.presentation.viewmodel.GenresViewModel
import com.app.presentation.viewmodel.SongsViewModel

@Composable
fun SearchScreen(songsViewModel: SongsViewModel = viewModel(), onSearch: (String) -> Unit = {}) {
    val suggestions by songsViewModel.songs.collectAsState()
    var query by remember { mutableStateOf("") }
    var isSuggestionVisible by remember { mutableStateOf(true) }

    var currentPage by remember { mutableStateOf(1) }

    LaunchedEffect(query) {
        if(query.isNotBlank()){
            songsViewModel.loadSongs(query, currentPage)
        }
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                isSuggestionVisible = true
            },
            label = { Text("Buscar cancion") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        if (query.isNotBlank() && isSuggestionVisible) {
            val filtered = suggestions
                .filter { it.title.contains(query, ignoreCase = true) }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(Color.Red)
            ) {
                items(filtered) { suggestion ->
                    Text(
                        text = suggestion.title + " (" + suggestion.artist.name+")",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                query = suggestion.title
                                isSuggestionVisible = false
                                onSearch(suggestion.title)
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}
