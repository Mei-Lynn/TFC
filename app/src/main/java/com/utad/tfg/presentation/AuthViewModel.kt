package com.utad.tfg.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.utad.tfg.local.daos.CharacterDao
import com.utad.tfg.security.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val characterDao: CharacterDao
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /** Reactive stream of the currently signed-in user (null = signed out). */
    val currentUser: StateFlow<FirebaseUser?> = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = authRepository.login(email, password)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.localizedMessage ?: "Login failed"
                )
            }
        }
    }

    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = authRepository.register(email, password)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.localizedMessage ?: "Registration failed"
                )
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = authRepository.loginWithGoogle(idToken)
                _authState.value = AuthState.Success(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.localizedMessage ?: "Google sign-in failed"
                )
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Idle

        viewModelScope.launch {
            characterDao.deleteAllCharacters()
        }
    }

    fun clearError() {
        _authState.value = AuthState.Idle
    }
}
