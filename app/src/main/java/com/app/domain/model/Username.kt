package com.app.domain.model

import com.google.firebase.firestore.DocumentId

data class Username(
    @DocumentId val code: String = "",
    val name: String = "",
    val codeUser: String = "",
)