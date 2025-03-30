package com.harmonyhub.app.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTokenRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TokenRepository {
    
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val TOKENS_COLLECTION = "tokens"
    }
    
    override suspend fun updateFcmToken(token: String) {
        val userId = auth.currentUser?.uid ?: return
        
        try {
            // Store token in the user's tokens collection
            val tokenData = mapOf(
                "token" to token,
                "createdAt" to System.currentTimeMillis(),
                "platform" to "android"
            )
            
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(TOKENS_COLLECTION)
                .document(token)
                .set(tokenData)
                .await()
                
            // Also update the latest token in the user document
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .update("fcmToken", token)
                .await()
                
            Timber.d("FCM token updated successfully for user: $userId")
        } catch (e: Exception) {
            Timber.e(e, "Failed to update FCM token")
        }
    }
}