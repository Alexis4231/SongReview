package com.app.data.repository

import com.app.domain.model.User
import com.app.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(firestore: FirebaseFirestore): UserRepository {
    private val usersCollection = firestore.collection("users")

    override suspend fun getByCode(code: String): User? {
        return try{
            val documentSnapshot = usersCollection.document(code).get().await()
            documentSnapshot.toObject(User::class.java)
        }catch (e: Exception){
            null
        }
    }

    override suspend fun save(user: User): Boolean {
        return try{
            usersCollection.add(user).await()
            true
        }catch (e: Exception){
            false
        }
    }

    override suspend fun delete(code: String): Boolean {
        return try {
            usersCollection.document(code).delete().await()
            true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    override suspend fun existUsername(name: String): Boolean {
        return try{
            val querySnapshot = usersCollection
                .whereEqualTo("name",name)
                .get()
                .await()
            querySnapshot.documents.isNotEmpty()
        }catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    override suspend fun getEmailByName(name: String): String {
        return try{
            val querySnapshot = usersCollection
                .whereEqualTo("name",name)
                .get()
                .await()
            val document = querySnapshot.documents.firstOrNull()
            document?.getString("email") ?: ""
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }
}