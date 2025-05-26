package com.app.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Request(
    @DocumentId val code: String = "",
    val codeIssuer: String = "",
    val codeReceiver: String = "",
    val status: String = "",
    @ServerTimestamp val date: Date? = null
)