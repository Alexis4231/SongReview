package com.app.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Review(
    @DocumentId val code: String = "",
    val codeUser: String,
    val codeSong: String,
    @ServerTimestamp val creationDate: Date? = null,
    val rating: Int = 0,
    val comment: String = ""
) {
    constructor(): this(codeUser = "", codeSong = "", rating = 0, comment = "")
}