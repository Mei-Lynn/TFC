package com.utad.tfg.security

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.utad.tfg.R
import com.utad.tfg.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val bannedSymbols = setOf('@', '#', '$', '%', '&', '*', '!', '?', '/', '\\', ' ', '<', '>')

    /** Emits the current [FirebaseUser] (or null) whenever the auth state changes. */
    val currentUser: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    val isLoggedIn: Boolean get() = firebaseAuth.currentUser != null

    /** Checks if the current user has the 'isAdmin' custom claim. */
    suspend fun isCurrentUserAdmin(): Boolean {
        val user = firebaseAuth.currentUser ?: return false
        return try {
            val tokenResult = user.getIdToken(false).await()
            tokenResult.claims["isAdmin"] as? Boolean ?: false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Register a new user with email, password, and username.
     * Returns the [FirebaseUser] on success or throws on failure.
     */
    suspend fun register(email: String, password: String, username: String): FirebaseUser {
        val invalidSymbol = username.firstOrNull { it in bannedSymbols }
        if (invalidSymbol != null) {
            val errorMessage = context.getString(
                R.string.error_invalid_username_symbol,
                invalidSymbol.toString()
            )
            throw FirebaseAuthInvalidCredentialsException("ERROR_INVALID_USERNAME", errorMessage)
        }

        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw IllegalStateException(context.getString(R.string.registration_user_null))

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        user.updateProfile(profileUpdates).await()

        val userModel = User(
            uid = user.uid,
            email = email,
            username = username
        )
        firestore.collection("users")
            .document(user.uid)
            .set(userModel)
            .await()

        return user
    }

    /**
     * Sign in an existing user with email and password.
     */
    suspend fun login(emailOrUsername: String, password: String): FirebaseUser {
        var credential = emailOrUsername

        if (!emailOrUsername.contains("@")){
            val query = firestore.collection("users")
                .whereEqualTo("username", emailOrUsername)
                .get()
                .await()

            if (query.isEmpty) {
                throw IllegalArgumentException(context.getString(R.string.username_not_found))
            }

            credential = query.documents.first().getString("email")
                ?: throw IllegalStateException(context.getString(R.string.user_record_missing_email))
        }

        val result = firebaseAuth.signInWithEmailAndPassword(credential, password).await()
        return result.user ?: throw IllegalStateException(context.getString(R.string.login_user_null))
    }

    /**
     * Sign in with a Google ID token obtained from Credential Manager.
     */
    suspend fun loginWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user ?: throw IllegalStateException(context.getString(R.string.google_signin_user_null))
    }

    /** Sign out the current user. */
    fun signOut() {
        firebaseAuth.signOut()
    }
}
