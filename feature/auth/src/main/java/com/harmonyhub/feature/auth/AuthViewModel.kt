package com.harmonyhub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harmonyhub.feature.auth.domain.AuthRepository
import com.harmonyhub.feature.auth.domain.AuthState
import com.harmonyhub.feature.auth.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel responsible for handling authentication related logic
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    init {
        // Check if the user is already signed in and update state accordingly
        viewModelScope.launch {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    _authState.update { 
                        it.copy(
                            isAuthenticated = true,
                            user = currentUser,
                            isLoading = false
                        )
                    }
                } else {
                    _authState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error checking auth state")
                _authState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    /**
     * Process the ID token from Google Sign-In and authenticate with Firebase
     */
    fun signInWithGoogle(idToken: String) {
        _authState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            try {
                val user = authRepository.signInWithGoogle(idToken)
                _authState.update { 
                    it.copy(
                        isAuthenticated = true,
                        user = user,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Google sign-in failed")
                _authState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Google sign-in failed"
                    )
                }
            }
        }
    }

    /**
     * Sign out the current user
     */
    fun signOut() {
        _authState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _authState.update { 
                    it.copy(
                        isAuthenticated = false,
                        user = null,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Sign out failed")
                _authState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Sign out failed"
                    )
                }
            }
        }
    }

    /**
     * Clear any error state
     */
    fun clearError() {
        _authState.update { it.copy(error = null) }
    }
}