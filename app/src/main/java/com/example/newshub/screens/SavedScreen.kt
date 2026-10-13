package com.example.newshub.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newshub.BookmarkRepository
import com.example.newshub.SavedArticle

// Shows a small popup with the result of an operation
private fun toastResult(context: Context, okMessage: String, success: Boolean, error: String?) {
    Toast.makeText(
        context,
        if (success) okMessage else "Failed: $error",
        Toast.LENGTH_SHORT
    ).show()
}

// The screen that lists the user's saved articles
@Composable
fun SavedScreen(navController: NavController? = null) {
    // The current list of bookmarks, loading flag, and error text
    var bookmarks by remember { mutableStateOf<List<SavedArticle>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Start listening to Firestore when the screen opens,
    // and stop listening (onDispose) when the screen closes
    DisposableEffect(Unit) {
        val registration = BookmarkRepository.listenToBookmarks(
            onChange = { list ->
                bookmarks = list
                loading = false
            },
            onError = { message ->
                error = message
                loading = false
            }
        )
        onDispose { registration?.remove() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title on the left, Back button on the right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Saved articles", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { navController?.popBackStack() }) {
                Text("Back")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Show exactly one of these, depending on the state
        when {
            loading -> Text("Loading...")
            error != null -> Text(error ?: "", color = MaterialTheme.colorScheme.error)
            bookmarks.isEmpty() -> Text("No saved articles yet. Open an article and tap Save.")
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // key = articleId helps Compose track each card as the list changes
                items(bookmarks, key = { it.articleId }) { article ->
                    SavedArticleCard(article)
                }
            }
        }
    }
}

// One bookmark card with its Update and Delete controls
@Composable
fun SavedArticleCard(article: SavedArticle) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    // The text typed in the note box. It resets if the saved note changes.
    var noteText by remember(article.articleId, article.note) { mutableStateOf(article.note) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.source,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            // UPDATE: mark as read / unread
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = article.markedRead,
                    onCheckedChange = { checked ->
                        BookmarkRepository.setRead(article.articleId, checked) { success, error ->
                            if (!success) toastResult(context, "", false, error)
                        }
                    }
                )
                Text("Mark as read")
            }

            // UPDATE: add or edit a note
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // UPDATE: save the note text to Firestore
                Button(onClick = {
                    BookmarkRepository.setNote(article.articleId, noteText) { success, error ->
                        toastResult(context, "Note saved", success, error)
                    }
                }) {
                    Text("Save note")
                }

                // DELETE: remove this bookmark
                OutlinedButton(onClick = {
                    BookmarkRepository.deleteBookmark(article.articleId) { success, error ->
                        toastResult(context, "Deleted", success, error)
                    }
                }) {
                    Text("Delete")
                }
            }

            // Open the original article in the browser, only if there is a link
            if (article.url.isNotBlank()) {
                TextButton(onClick = { uriHandler.openUri(article.url) }) {
                    Text("Read full article")
                }
            }
        }
    }
}