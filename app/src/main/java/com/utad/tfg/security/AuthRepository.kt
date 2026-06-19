package com.utad.tfg.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    /** Emits the current [FirebaseUser] (or null) whenever the auth state changes. */
    val currentUser: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    val isLoggedIn: Boolean get() = firebaseAuth.currentUser != null

    /**
     * Register a new user with email and password.
     * Returns the [FirebaseUser] on success or throws on failure.
     */
    suspend fun register(email: String, password: String): FirebaseUser {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return result.user ?: throw IllegalStateException("Registration succeeded but user is null")
    }

    /**
     * Sign in an existing user with email and password.
     */
    suspend fun login(email: String, password: String): FirebaseUser {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: throw IllegalStateException("Login succeeded but user is null")
    }

    /**
     * Sign in with a Google ID token obtained from Credential Manager.
     */
    suspend fun loginWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user ?: throw IllegalStateException("Google sign-in succeeded but user is null")
    }

    /** Sign out the current user. */
    fun signOut() {
        firebaseAuth.signOut()
    }
}
