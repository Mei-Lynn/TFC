package com.utad.tfg.remote

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val application: Application,
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
        val processedData = processDataForUpload(data)
        firestore.collection(collection).add(processedData).await()
    }

    suspend fun updateDocument(collection: String, docId: String, data: Map<String, Any>) {
        val processedData = processDataForUpload(data)
        firestore.collection(collection).document(docId).set(processedData).await()
    }

    suspend fun deleteDocument(collection: String, docId: String) {
        firestore.collection(collection).document(docId).delete().await()
    }

    private fun processDataForUpload(data: Map<String, Any>): Map<String, Any> {
        val processed = data.toMutableMap()
        val imgUri = processed["imgUri"] as? String
        if (imgUri != null && (imgUri.startsWith("content://") || imgUri.startsWith("file://"))) {
            val base64Str = encodeImageToBase64(imgUri)
            if (base64Str != null) {
                processed["imgUri"] = base64Str
            }
        }
        return processed
    }

    private fun encodeImageToBase64(localUri: String): String? {
        return try {
            val uri = Uri.parse(localUri)
            application.contentResolver.openInputStream(uri)?.use { input ->
                val original = BitmapFactory.decodeStream(input)
                val maxDimension = 400
                val scaled = if (original.width > maxDimension || original.height > maxDimension) {
                    val ratio = maxDimension.toFloat() / maxOf(original.width, original.height)
                    Bitmap.createScaledBitmap(
                        original,
                        (original.width * ratio).toInt(),
                        (original.height * ratio).toInt(),
                        true
                    )
                } else original

                val outputStream = ByteArrayOutputStream()
                scaled.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
                if (scaled !== original) scaled.recycle()
                original.recycle()

                val byteArray = outputStream.toByteArray()
                val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                "data:image/jpeg;base64,$base64String"
            }
        } catch (e: Exception) {
            Log.e("AdminRepository", "Failed to encode image to Base64", e)
            null
        }
    }
}
