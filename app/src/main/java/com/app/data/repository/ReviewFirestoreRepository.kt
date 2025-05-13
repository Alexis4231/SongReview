package com.app.data.repository

import com.app.domain.model.Review
import com.app.domain.repository.ReviewRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewFirestoreRepository(firestore: FirebaseFirestore): ReviewRepository {
    private val reviewsCollection = firestore.collection("reviews")

    override suspend fun save(review: Review): Boolean {
        return try{
            reviewsCollection.add(review).await()
            true
        }catch (e: Exception){
            false
        }
    }
}