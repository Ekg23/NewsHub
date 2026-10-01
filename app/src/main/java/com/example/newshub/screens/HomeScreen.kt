@file:Suppress("MISSING_DEPENDENCY_SUPERCLASS_IN_TYPE_ARGUMENT")

package com.example.newshub.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn      // a scrolling list that only draws visible items
import androidx.compose.foundation.lazy.items           // lets us loop through a list inside LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect          // runs code once when the screen appears
import androidx.compose.runtime.mutableStateOf          // creates a value Compose watches for changes
import androidx.compose.runtime.remember                // keeps that value between redraws
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode // tells us if we're in the preview panel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController               // lets this screen trigger navigation to another screen
// Removed: getValue and setValue (not needed without "by"), and the duplicate imports

// @Composable means "this function draws part of the UI".
// It takes navController so it can tell the app to move to a different screen.
// Shows this screen in Android Studio's preview panel
@Preview(showBackground = true)
// @Composable means "this function draws part of the UI"
@Composable
// navController lets this screen move to other screens.
// "? = null" makes it optional, so the preview can run without a real one.
fun HomeScreen(navController: NavController? = null) {

    // The list of articles the screen shows.
    // mutableStateOf = a value Compose watches, so the screen redraws when it changes.
    // remember = keeps the value between redraws, so it isn't reset to empty each time.
    // It starts as an empty list and is filled after the news loads.
    val articles = remember { mutableStateOf<List<Article>>(emptyList()) }

    // A message to show if loading fails.
    // String? means "text or nothing", and it starts as null (no error).
    val error = remember { mutableStateOf<String?>(null) }

    // true when the code runs inside the preview panel, false on a real device
    val isPreview = LocalInspectionMode.current

    // LaunchedEffect runs its code once, when the screen first appears.
    // "Unit" is a fixed key, which means "don't run it again on every redraw".
    LaunchedEffect(Unit) {
        // Skip the internet request in the preview, since previews can't use the internet
        if (!isPreview) {
            try {
                // Fetch the news and store it. .value is how you write into a state holder.
                // Writing to it makes the screen redraw with the new list.
                articles.value = fetchArticles()
            } catch (e: Exception) {
                // Something failed (no internet, bad key, etc.), so store a message to display.
                // ${e.message} adds the real reason to the text.
                error.value = "Could not load news: ${e.message}"
            }
        }
    }

    // Use your fake sampleArticles in the preview, and the fetched list on a device.
    // .value is how you read from a state holder.
    val listToShow = if (isPreview) sampleArticles else articles.value

    // Column stacks its children from top to bottom
    Column(
        modifier = Modifier.fillMaxSize()   // fill the whole screen
    ) {
        // Row places its children side by side: title on the left, button on the right
        Row(
            modifier = Modifier
                .fillMaxWidth()                                    // stretch across the screen
                .padding(horizontal = 16.dp, vertical = 8.dp),     // space around the row
            horizontalArrangement = Arrangement.SpaceBetween,      // push the two children to opposite ends
            verticalAlignment = Alignment.CenterVertically         // line them up vertically
        ) {
            // The screen title
            Text(
                text = "NewsHub",
                style = MaterialTheme.typography.headlineMedium
            )

            // Button that opens the Profile screen.
            // "?." means "only navigate if navController exists" (it's null in the preview).
            TextButton(onClick = { navController?.navigate(Routes.PROFILE) }) {
                Text("Profile")
            }
        }

        // Shows the greeting only after a name has been saved
        if (UserSettings.displayName.value.isNotBlank()) {
            Text(
                text = "Hello, ${UserSettings.displayName.value}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Show the error text only if there is one.
        // ?.let { } runs its code only when the value is not null.
        error.value?.let { Text(it) }

        // LazyColumn is a scrolling list that only draws the items currently visible
        LazyColumn(
            modifier = Modifier.weight(1f),   // take all the space left below the Row
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),  // spacing around the whole list
            verticalArrangement = Arrangement.spacedBy(12.dp)                     // gap between cards
        ) {
            // Create one ArticleCard for each article in the list
            items(listToShow) { article ->
                ArticleCard(
                    article,
                    onClick = {
                        // Remember which article was tapped, so the Article screen can show it
                        SelectedArticle.article = article
                        // Then open the Article screen
                        navController?.navigate(Routes.ARTICLE)
                    }
                )
            }
        }
    }
}