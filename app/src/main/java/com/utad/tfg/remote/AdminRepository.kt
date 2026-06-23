package com.utad.tfg.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getDocuments(collection: String): List<Map<String, Any>> {
        return try {
            val snapshot = firestore.collection(collection).get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.data?.toMutableMap()?.apply {
                    put("_id", doc.id) // Use _id so it doesn't conflict with potential 'id' fields
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addDocument(collection: String, data: Map<String, Any>) {
        firestore.collection(collection).add(data).await()
    }

    suspend fun updateDocument(collection: String, docId: String, data: Map<String, Any>) {
        firestore.collection(collection).document(docId).set(data).await()
    }

    suspend fun deleteDocument(collection: String, docId: String) {
        firestore.collection(collection).document(docId).delete().await()
    }
}
