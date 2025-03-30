package com.harmonyhub.feature.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.harmonyhub.feature.auth.domain.AuthRepository
import com.harmonyhub.feature.auth.domain.User
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth
    
    companion object {
        private const val USERS_COLLECTION = "users"
    }

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        
        return User(
            id = firebaseUser.uid,
            displayName = firebaseUser.displayName,
            email = firebaseUser.email,
            photoUrl = firebaseUser.photoUrl?.toString()
        )
    }

    override suspend fun signInWithGoogle(idToken: String): User {
        try {
            // Get the credential from the ID token
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            
            // Sign in with Firebase Authentication
            val authResult = auth.signInWithCredential(credential).await()
            
            // Get the newly signed-in user
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("Firebase authentication successful but user is null")
            
            // Create a user object
            val user = User(
                id = firebaseUser.uid,
                displayName = firebaseUser.displayName,
                email = firebaseUser.email,
                photoUrl = firebaseUser.photoUrl?.toString()
            )
            
            // Store or update user data in Firestore
            storeUserInFirestore(user)
            
            return user
        } catch (e: Exception) {
            Timber.e(e, "Failed to sign in with Google")
            throw e
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
    
    /**
     * Store or update user data in Firestore
     */
    private suspend fun storeUserInFirestore(user: User) {
        try {
            val userMap = mapOf(
                "displayName" to user.displayName,
                "email" to user.email,
                "photoUrl" to user.photoUrl,
                "lastSignIn" to System.currentTimeMillis()
            )
            
            firestore.collection(USERS_COLLECTION)
                .document(user.id)
                .set(userMap)
                .await()
                
            Timber.d("User stored in Firestore: ${user.id}")
        } catch (e: Exception) {
            // Non-fatal error, log but don't throw (authentication still succeeded)
            Timber.e(e, "Failed to store user in Firestore")
        }
    }
}