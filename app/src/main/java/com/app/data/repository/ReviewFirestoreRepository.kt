package com.app.data.repository

import com.app.domain.model.Review
import com.app.domain.model.SongDB
import com.app.domain.repository.ReviewRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    override suspend fun getReviews(): List<Review> {
        return try{
            val documentSnapshot = reviewsCollection.get().await()
            documentSnapshot.toObjects(Review::class.java)
        }catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getReviewsByCodeSong(codeSong: String): List<Review> {
        return try{
            val querySnapshot = reviewsCollection
                .whereEqualTo("codeSong",codeSong)
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .await()
            querySnapshot.toObjects(Review::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }
}