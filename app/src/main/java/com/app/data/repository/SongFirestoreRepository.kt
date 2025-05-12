package com.app.data.repository

import com.app.domain.model.SongDB
import com.app.domain.repository.SongRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SongFirestoreRepository(firestore: FirebaseFirestore): SongRepository {
    private val songsCollection = firestore.collection("songs")

    override suspend fun save(song: SongDB): Boolean {
        return try{
            songsCollection.add(song).await()
            true
        }catch (e: Exception){
            false
        }
    }
}