package com.app.domain.model

import com.google.firebase.firestore.DocumentId

data class Request(
    @DocumentId val code: String = "",
    val codeIssuer: String,
    val codeReceiver: String,
    val status: String
)