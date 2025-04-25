package com.app.domain.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val code: String = "",
    val name: String,
    val email: String
){
    constructor(): this(name = "",email="")
}