package com.app.data.repository

import com.app.domain.model.Request
import com.app.domain.model.User
import com.app.domain.model.Username
import com.app.domain.repository.RequestRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class RequestFirestoreRepository(firestore: FirebaseFirestore): RequestRepository {
    private val requestsCollection = firestore.collection("requests")

    override suspend fun save(usernameReceiver: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified) return false
            val querySnapshot = Firebase.firestore.collection("usernames")
                .whereEqualTo("name",usernameReceiver)
                .get()
                .await()
            val receiverDoc = querySnapshot.documents.firstOrNull() ?: return false
            val codeReceiver = receiverDoc.getString("codeUser") ?: return false
            val existingRequestLeft = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeReceiver)
                .limit(1)
                .get()
                .await()
            if(!existingRequestLeft.isEmpty){
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
            val querySnapshot = Firebase.firestore.collection("usernames")
                .whereEqualTo("name", username)
                .get()
                .await()
            val userDoc = querySnapshot.documents.firstOrNull() ?: return null
            val codeUser = userDoc.getString("codeUser") ?: return null
            val existingRequestLeft = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeUser)
                .limit(1)
                .get()
                .await()
            val existingRequestRight = requestsCollection
                .whereEqualTo("codeIssuer", codeUser)
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

    override suspend fun acceptRequest(username: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified) return false
            val querySnapshot = Firebase.firestore.collection("usernames")
                .whereEqualTo("name", username)
                .get()
                .await()
            val issuerDoc = querySnapshot.documents.firstOrNull() ?: return false
            val codeIssuer = issuerDoc.getString("codeUser") ?: return false
            val existingRequest = requestsCollection
                .whereEqualTo("codeIssuer", codeIssuer)
                .whereEqualTo("codeReceiver", currentUser.uid)
                .whereEqualTo("status","PENDING")
                .limit(1)
                .get()
                .await()
            if(!existingRequest.isEmpty){
                val requestDoc = existingRequest.documents.first()
                val document = requestsCollection.document(requestDoc.id)
                val updates = mapOf(
                    "status" to "ACCEPTED",
                    "date" to FieldValue.serverTimestamp()
                )
                document.set(updates, SetOptions.merge()).await()
            }else{
                return false
            }
            true
        }catch(e: Exception){
            false
        }
    }

    override suspend fun deleteRequest(username: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified) return false
            val querySnapshot = Firebase.firestore.collection("usernames")
                .whereEqualTo("name", username)
                .get()
                .await()
            val userDoc = querySnapshot.documents.firstOrNull() ?: return false
            val codeUser = userDoc.getString("codeUser") ?: return false
            val existingRequestLeft = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeUser)
                .whereEqualTo("status","ACCEPTED")
                .limit(1)
                .get()
                .await()
            val existingRequestRight = requestsCollection
                .whereEqualTo("codeIssuer", codeUser)
                .whereEqualTo("codeReceiver", currentUser.uid)
                .whereEqualTo("status","ACCEPTED")
                .limit(1)
                .get()
                .await()
            if(!existingRequestLeft.isEmpty){
                val requestDoc = existingRequestLeft.documents.first()
                requestsCollection.document(requestDoc.id)
                    .delete()
                    .await()
            }else if(!existingRequestRight.isEmpty){
                val requestDoc = existingRequestRight.documents.first()
                requestsCollection.document(requestDoc.id)
                    .delete()
                    .await()
            }else{
                return false
            }
            true
        }catch (e: Exception){
            false
        }
    }

    override suspend fun cancelRequest(username: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified) return false
            val querySnapshot = Firebase.firestore.collection("usernames")
                .whereEqualTo("name", username)
                .get()
                .await()
            val userDoc = querySnapshot.documents.firstOrNull() ?: return false
            val codeReceiver = userDoc.getString("codeUser") ?: return false
            val existingRequest = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("codeReceiver", codeReceiver)
                .whereEqualTo("status","PENDING")
                .limit(1)
                .get()
                .await()
            if(!existingRequest.isEmpty){
                val requestDoc = existingRequest.documents.first()
                requestsCollection.document(requestDoc.id)
                    .delete()
                    .await()
            }else{
                return false
            }
            true
        }catch (e: Exception){
            false
        }
    }

    override suspend fun getFollowers(): List<String> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try{
            if(!currentUser.isEmailVerified) return emptyList()
            val result = mutableListOf<Pair<String,Date?>>()
            val existingRequestsLeft = requestsCollection
                .whereEqualTo("codeIssuer", currentUser.uid)
                .whereEqualTo("status","ACCEPTED")
                .get()
                .await()
            val existingRequestsRight = requestsCollection
                .whereEqualTo("codeReceiver", currentUser.uid)
                .whereEqualTo("status","ACCEPTED")
                .get()
                .await()
            if(!existingRequestsLeft.isEmpty){
                for(request in existingRequestsLeft.toObjects(Request::class.java)){
                    val code = request.codeReceiver
                    val date = request.date
                    val documentSnapshot = Firebase.firestore.collection("usernames")
                        .whereEqualTo("codeUser",code)
                        .get()
                        .await()
                    val usernameDoc = documentSnapshot.documents.firstOrNull()
                    val username = usernameDoc?.getString("name")
                    if (username != null) {
                        result.add(username to date)
                    }
                }
            }
            if(!existingRequestsRight.isEmpty){
                for(request in existingRequestsRight.toObjects(Request::class.java)){
                    val code = request.codeIssuer
                    val date = request.date
                    val documentSnapshot = Firebase.firestore.collection("usernames")
                        .whereEqualTo("codeUser",code)
                        .get()
                        .await()
                    val usernameDoc = documentSnapshot.documents.firstOrNull()
                    val username = usernameDoc?.getString("name")
                    if (username != null) {
                        result.add(username to date)
                    }
                }
            }
            if(result.isNotEmpty()){
                return result
                    .sortedByDescending { it.second }
                    .map { it.first }
            }else{
                return emptyList()
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override suspend fun getRequestFollowers(): List<String> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try{
            if(!currentUser.isEmailVerified) return emptyList()
            val result = mutableListOf<Pair<String,Date?>>()
            val existingRequestsRight = requestsCollection
                .whereEqualTo("codeReceiver", currentUser.uid)
                .whereEqualTo("status","PENDING")
                .get()
                .await()
            if(!existingRequestsRight.isEmpty){
                for(request in existingRequestsRight.toObjects(Request::class.java)){
                    val code = request.codeIssuer
                    val date = request.date
                    val documentSnapshot = Firebase.firestore.collection("usernames")
                        .whereEqualTo("codeUser",code)
                        .get()
                        .await()
                    val usernameDoc = documentSnapshot.documents.firstOrNull()
                    val username = usernameDoc?.getString("name")
                    if (username != null) {
                        result.add(username to date)
                    }
                }
            }
            if(result.isNotEmpty()){
                return result
                    .sortedByDescending { it.second }
                    .map { it.first }
            }else{
                return emptyList()
            }
        }catch (e: Exception){
            emptyList()
        }
    }
}