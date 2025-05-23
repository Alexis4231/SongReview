package com.app.domain.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Review(
    @DocumentId val code: String = "",
    @ServerTimestamp val creationDate: Date? = null,
    val codeUser: String = "",
    val publicReview: PublicReview = PublicReview(),
)

data class PublicReview(
    val codeSong: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val username: String = ""
)