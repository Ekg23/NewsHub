package com.example.newshub.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.newshub.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// This object just holds the "names" (route strings) for each screen.
// Instead of typing "home", "article", "profile" as raw text everywhere
object Routes {
    const val HOME = "home" // route for the firs screen the user see
    const val ARTICLE = "article" // route for the article detail screen
    const val PROFILE = "profile" // route for the profile screen

    const val SAVED = "saved"     // route for the saved articles screen
}

// One screen that works as both "Log in" and "Sign up"
@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    // Text typed into the fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // true = sign-up version, false = login version
    var isSignUpMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isSignUpMode) "Create account" else "Welcome back",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (6+ characters)") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(), // hides typed characters
            modifier = Modifier.fillMaxWidth()
        )

        // Show an error message only when there is one
        authViewModel.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (isSignUpMode) authViewModel.signUp(email, password)
                else authViewModel.logIn(email, password)
            },
            // Disabled while loading or when a field is empty
            enabled = !authViewModel.isLoading && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSignUpMode) "Sign up" else "Log in")
        }

        // Switch between login and sign up
        TextButton(onClick = {
            isSignUpMode = !isSignUpMode
            authViewModel.clearError()
        }) {
            Text(if (isSignUpMode) "Already have an account? Log in" else "No account? Sign up")
        }
    }
}