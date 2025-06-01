package com.app.data.repository

import com.app.domain.model.SongDB
import com.app.domain.model.User
import com.app.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(firestore: FirebaseFirestore): UserRepository {
    private val usersCollection = firestore.collection("users")

    override suspend fun getByCode(code: String): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return null
        return try{
            val documentSnapshot = usersCollection.document(code).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            if(user?.code == currentUser.uid && currentUser.isEmailVerified){
                user
            }else{
                null
            }
        }catch (e: Exception){
            null
        }
    }

    override suspend fun delete(code: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try {
            val documentSnapshot = usersCollection.document(code)
            val snapshot = documentSnapshot.get().await()
            val user = snapshot.toObject(User::class.java)
            if(user?.code == currentUser.uid && currentUser.isEmailVerified){
                documentSnapshot.delete().await()
                true
            }else{
                false
            }
        }catch (e: Exception){
            false
        }
    }

    override suspend fun getEmailByName(name: String): String {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return ""
        return try{
            val querySnapshot = usersCollection
                .whereEqualTo("name",name)
                .get()
                .await()
            val document = querySnapshot.documents.firstOrNull()
            val email = document?.getString("email") ?: ""
            if(document?.getString("code") == currentUser.uid) email else ""
        }catch (e: Exception){
            ""
        }
    }

    override suspend fun updateFcmToken(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try{
            if(!currentUser.isEmailVerified) return false
            val documentSnapshot = usersCollection.document(currentUser.uid).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            val token = FirebaseMessaging.getInstance().token.await()
            if(user?.fcmToken == null || user.fcmToken != token) {
                usersCollection.document(currentUser.uid).update("fcmToken", token).await()
                true
            }else{
                false
            }
        }catch (e: Exception){
            false
        }
    }
}