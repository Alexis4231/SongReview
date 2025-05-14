package com.app.domain.model

import com.google.firebase.firestore.DocumentId

data class SongDB(
    @DocumentId val code: String = "",
    val title: String = "",
    val artist: String = "",
    val genre: String = "",
)