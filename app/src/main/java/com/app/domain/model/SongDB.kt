package com.app.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SongDB(
    @DocumentId val code: String = "",
    val title: String = "",
    val artist: String = "",
    val genre: String = "",
    @ServerTimestamp val creationDate: Date? = null
)