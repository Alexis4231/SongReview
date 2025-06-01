package com.app.data.repository

import com.app.domain.model.PublicReview
import com.app.domain.model.Review
import com.app.domain.repository.ReviewRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ReviewFirestoreRepository(firestore: FirebaseFirestore): ReviewRepository {
    private val reviewsCollection = firestore.collection("reviews")

    override suspend fun save(review: Review): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified || review.codeUser != currentUser.uid ) return false
            val songExists = review.publicReview?.let {
                Firebase.firestore.collection("songs")
                    .document(it.codeSong)
                    .get()
                    .await()
                    .exists()
            }
            if(!songExists!!) return false
            reviewsCollection.add(review).await()
            true
        }catch (e: Exception){
            false
        }
    }

    override suspend fun getReviews(): List<PublicReview> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try {
            if(!currentUser.isEmailVerified) return emptyList()
            val documentSnapshot = reviewsCollection.get().await()
            documentSnapshot.documents.mapNotNull { doc ->
                doc.toObject(Review::class.java)?.publicReview
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override suspend fun getReviewsByCodeSong(codeSong: String): List<PublicReview> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try{
            if(!currentUser.isEmailVerified) return emptyList()
            val querySnapshot = reviewsCollection
                .whereEqualTo("publicReview.codeSong",codeSong)
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .await()
            querySnapshot.documents.mapNotNull {doc ->
                doc.toObject(Review::class.java)?.publicReview
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override suspend fun getReviewsByCodeUser(codeUser: String): List<Review> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try{
            if(codeUser != currentUser.uid || !currentUser.isEmailVerified) return emptyList()
            val querySnapshot = reviewsCollection
                .whereEqualTo("codeUser",codeUser)
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .await()
            querySnapshot.toObjects(Review::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

    override suspend fun getReviewsByUsernameFollower(username: String): List<PublicReview> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try{
            if(!currentUser.isEmailVerified) return emptyList()
            val querySnapshotUsers = Firebase.firestore.collection("usernames")
                .whereEqualTo("name", username)
                .get()
                .await()
            val userDoc = querySnapshotUsers.documents.firstOrNull() ?: return emptyList()
            val codeUser = userDoc.getString("codeUser") ?: return emptyList()
            val existingRequestLeft = Firebase.firestore.collection("requests")
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeUser)
                .whereEqualTo("status","ACCEPTED")
                .limit(1)
                .get()
                .await()
            val existingRequestRight = Firebase.firestore.collection("requests")
                .whereEqualTo("codeIssuer", codeUser)
                .whereEqualTo("codeReceiver", currentUser.uid)
                .whereEqualTo("status","ACCEPTED")
                .limit(1)
                .get()
                .await()
            if(!existingRequestRight.isEmpty || !existingRequestLeft.isEmpty){
               val querySnapshotReviews =  reviewsCollection
                   .whereEqualTo("codeUser",codeUser)
                   .orderBy("creationDate", Query.Direction.DESCENDING)
                   .get()
                   .await()
                querySnapshotReviews.documents.mapNotNull {doc ->
                    doc.toObject(Review::class.java)?.publicReview
                }
            }else {
                return emptyList()
            }
        }catch (e: Exception){
            emptyList()
        }
    }
}