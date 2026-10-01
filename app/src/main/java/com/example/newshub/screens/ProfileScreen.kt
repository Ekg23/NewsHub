package com.example.newshub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

@Preview(showBackground = true)
@Composable
fun ProfileScreen(navController: NavController? = null) {
    // What the user is typing right now. It starts with the saved name.
    val typedName = remember { mutableStateOf(UserSettings.displayName.value) }

    // The name to show: the saved one, or "Guest" if nothing is saved yet
    val shownName = UserSettings.displayName.value.ifBlank { "Guest" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circle showing the first letter of the saved name
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = shownName.first().uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = shownName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        // The box where the user types their name
        OutlinedTextField(
            value = typedName.value,
            onValueChange = { typedName.value = it },   // update as each letter is typed
            label = { Text("Display name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        // Saves the typed name so Home can use it
        Button(
            onClick = { UserSettings.displayName.value = typedName.value.trim() },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Save name")
        }

        Button(
            onClick = { navController?.popBackStack(Routes.HOME, inclusive = false) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Back to Home")
        }
    }
}