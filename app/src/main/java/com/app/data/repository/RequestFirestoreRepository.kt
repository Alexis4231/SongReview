package com.app.data.repository

import com.app.domain.model.Request
import com.app.domain.repository.RequestRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RequestFirestoreRepository(firestore: FirebaseFirestore): RequestRepository {
    private val requestsCollection = firestore.collection("requests")

    override suspend fun save(request: Request): Boolean {
        return try{
            requestsCollection.add(request).await()
            true
        }catch (e: Exception){
            false
        }
    }

    override suspend fun getRequestsByCodeIssuer(codeIssuer: String): List<Request> {
        return try{
            val querySnapshot = requestsCollection
                .whereEqualTo("codeIssuer",codeIssuer)
                .get()
                .await()
            querySnapshot.toObjects(Request::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

    override suspend fun getStatusByCodeIssuerAndCodeReceiver(
        codeIssuer: String,
        codeReceiver: String
    ): String? {
        return try{
            val documentSnapshot = requestsCollection
                .whereEqualTo("codeIssuer",codeIssuer)
                .whereEqualTo("codeReceiver",codeReceiver)
                .get()
                .await()
            if(!documentSnapshot.isEmpty){
                val document = documentSnapshot.documents[0]
                document?.getString("status") ?: ""
            }else{
                null
            }
        }catch (e: Exception){
            null
        }
    }

    override suspend fun acceptRequest(codeIssuer: String, codeReceiver: String): Boolean {
        return try{
            val documentSnapshot = requestsCollection
                .whereEqualTo("codeIssuer",codeIssuer)
                .whereEqualTo("codeReceiver",codeReceiver)
                .whereEqualTo("status","PENDING")
                .limit(1)
                .get()
                .await()
            if(documentSnapshot.isEmpty){
                false
            }else{
                val document = documentSnapshot.documents.first()
                requestsCollection.document(document.id)
                    .update("state","ACCEPTED")
                    .await()
                true
            }
            true
        }catch (e: Exception){
            false
        }
    }
}