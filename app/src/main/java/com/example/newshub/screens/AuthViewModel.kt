package com.example.newshub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

// Handles sign up, login and log out through Firebase Authentication

class AuthViewModel : ViewModel() {

    // our connection to firebase auth service
    private val auth = FirebaseAuth.getInstance()

    // true if someone is signed in
    var isLoggedIn by mutableStateOf(auth.currentUser != null)
        private set

    // Error text to show on screen (null means no error)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // true while a request is in progress
    var isLoading by mutableStateOf(false)
        private set

    // Create a new account with email and password
    fun signUp(email: String, password: String) {
        isLoading =true
        errorMessage = null
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    isLoggedIn = true  // Firebase signs the new user in automatically
                } else {
                    errorMessage = task.exception?.message ?: "Sign up failed"
                }

            }}

    // Sign in to an existing account
    fun logIn(email: String, password: String) {
        isLoading = true
        errorMessage = null
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    isLoggedIn = true
                } else {
                    errorMessage = task.exception?.message ?: "Login failed"
                }
            }
    }

    // Sign out the current user
    fun logOut() {
        auth.signOut()
        isLoggedIn = false
    }

    // Clears the error text
    fun clearError() {
        errorMessage = null
    }
}