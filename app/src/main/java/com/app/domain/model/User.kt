package com.app.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    @DocumentId val code: String = "",
    val name: String = "",
    val email: String = "",
    @ServerTimestamp val creationDate: Date? = null
)