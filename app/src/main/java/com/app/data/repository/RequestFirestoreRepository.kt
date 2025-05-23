package com.app.data.repository

import com.app.domain.model.Request
import com.app.domain.repository.RequestRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class RequestFirestoreRepository(firestore: FirebaseFirestore): RequestRepository {
    private val requestsCollection = firestore.collection("requests")

    override suspend fun save(usernameReceiver: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified) return false
            val querySnapshot = Firebase.firestore.collection("users")
                .whereEqualTo("name",usernameReceiver)
                .get()
                .await()
            val receiverDoc = querySnapshot.documents.firstOrNull() ?: return false
            val codeReceiver = receiverDoc.id
            val existingRequestLeft = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeReceiver)
                .limit(1)
                .get()
                .await()
            val existingRequestRight = requestsCollection
                .whereEqualTo("codeIssuer", codeReceiver)
                .whereEqualTo("codeReceiver", currentUser.uid)
                .limit(1)
                .get()
                .await()
            if(!existingRequestLeft.isEmpty || !existingRequestRight.isEmpty){
                return false
            }
            val request = Request(
                    codeIssuer = currentUser.uid,
                    codeReceiver = codeReceiver,
                    status = "PENDING"
                )
            requestsCollection.add(request).await()
            true
        }catch (e: Exception){
            false
        }
    }

    override suspend fun getStatus(username: String): Map<String,Boolean>? {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return null
        return try {
            if (!currentUser.isEmailVerified) return null
            val querySnapshot = Firebase.firestore.collection("users")
                .whereEqualTo("name", username)
                .get()
                .await()
            val receiverDoc = querySnapshot.documents.firstOrNull() ?: return null
            val codeReceiver = receiverDoc.id
            val existingRequestLeft = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeReceiver)
                .limit(1)
                .get()
                .await()
            val existingRequestRight = requestsCollection
                .whereEqualTo("codeIssuer", codeReceiver)
                .whereEqualTo("codeReceiver", currentUser.uid)
                .limit(1)
                .get()
                .await()
            if (!existingRequestLeft.isEmpty){
                val status = existingRequestLeft.documents.first().getString("status")
                if(status != null){
                    return mapOf(status to true)
                }
                return null
            }else if(!existingRequestRight.isEmpty){
                val status = existingRequestRight.documents.first().getString("status")
                if(status != null){
                    return mapOf(status to false)
                }
                return null
            }else{
                return null
            }
        }catch (e: Exception){
            return null
        }
    }
}