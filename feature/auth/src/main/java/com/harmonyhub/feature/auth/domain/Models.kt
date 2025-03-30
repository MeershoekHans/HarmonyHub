package com.harmonyhub.feature.auth.domain

/**
 * Represents a user in the application
 */
data class User(
    val id: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?
)

/**
 * Represents the current authentication state
 */
data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null
)

/**
 * Interface for authentication repository
 */
interface AuthRepository {
    /**
     * Get the current authenticated user or null if not authenticated
     */
    suspend fun getCurrentUser(): User?
    
    /**
     * Sign in with Google ID token
     */
    suspend fun signInWithGoogle(idToken: String): User
    
    /**
     * Sign out the current user
     */
    suspend fun signOut()
}