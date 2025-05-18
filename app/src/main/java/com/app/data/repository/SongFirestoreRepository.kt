package com.app.data.repository

import com.app.domain.model.Song
import com.app.domain.model.SongDB
import com.app.domain.model.User
import com.app.domain.repository.SongRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    override suspend fun getSongs(): List<SongDB> {
        return try{
            val documentSnapshot = songsCollection
                .orderBy("creationDate",Query.Direction.DESCENDING)
                .get()
                .await()
            documentSnapshot.toObjects(SongDB::class.java)
        }catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getByCode(code: String): SongDB? {
        return try{
            val documentSnapshot = songsCollection.document(code).get().await()
            documentSnapshot.toObject(SongDB::class.java)
        }catch (e: Exception){
            null
        }
    }

    override suspend fun getCodeByTitleAndArtist(title: String, artist: String): String? {
        return try{
            val documentSnapshot = songsCollection
                .whereEqualTo("title",title)
                .whereEqualTo("artist",artist)
                .get()
                .await()
            if(!documentSnapshot.isEmpty){
                val document = documentSnapshot.documents[0]
                document.id
            }else{
                null
            }
        }catch (e: Exception){
            null
        }
    }
}