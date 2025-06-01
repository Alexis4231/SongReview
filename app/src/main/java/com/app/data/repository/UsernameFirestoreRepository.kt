package com.app.data.repository

import com.app.domain.model.SongDB
import com.app.domain.model.User
import com.app.domain.model.Username
import com.app.domain.repository.UsernameRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UsernameFirestoreRepository(firestore: FirebaseFirestore): UsernameRepository {
    private val usernamesCollection = firestore.collection("usernames")

    override suspend fun save(username: Username): Boolean {
        return try {
            usernamesCollection.add(username).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun existUsername(name: String): Boolean {
        return try{
            val querySnapshot = usernamesCollection
                .whereEqualTo("name",name)
                .get()
                .await()
            querySnapshot.documents.isNotEmpty()
        }catch (e: Exception){
            false
        }
    }

    override suspend fun getUsernames(): List<String> {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try{
            if(!currentUser.isEmailVerified) return emptyList()
            val documentSnapshot = usernamesCollection
                .get()
                .await()
            documentSnapshot.toObjects(Username::class.java).mapNotNull { it.name }
        }catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun delete(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return false
        return try {
            val querySnapshot = usernamesCollection
                .whereEqualTo("codeUser",currentUser.uid)
                .get()
                .await()
            val document = querySnapshot.documents.firstOrNull()
            val username = document?.toObject(Username::class.java)
            if(username != null && currentUser.isEmailVerified){
                document.reference.delete().await()
                true
            }else{
                false
            }
        }catch (e: Exception){
            false
        }
    }
}