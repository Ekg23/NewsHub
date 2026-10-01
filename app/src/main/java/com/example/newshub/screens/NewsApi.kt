package com.example.newshub.screens

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.runtime.mutableStateOf
import com.example.newshub.BuildConfig

// Put your NEW key between the quotes. Don't push a real key to a public GitHub repo.
const val NEWS_API_KEY = BuildConfig.NEWS_API_KEY

suspend fun fetchArticles(): List<Article> = withContext(Dispatchers.IO) {
    val url = URL("https://newsapi.org/v2/top-headlines?country=us&apiKey=$NEWS_API_KEY")
    val connection = url.openConnection() as HttpURLConnection

    try {
        // NEW: identify the app to the server, since some servers reject requests without this
        connection.setRequestProperty("User-Agent", "NewsHubApp")

        // NEW: ask for the HTTP status number (200 = OK, 401 = bad key, 429 = too many requests...)
        val code = connection.responseCode

        // NEW: if it isn't 200, read the server's explanation and show it instead of a generic message
        if (code != 200) {
            val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() }
            throw Exception("HTTP $code: $errorText")
        }

        // Read the whole response as one piece of text
        val text = connection.inputStream.bufferedReader().use { it.readText() }

        // Turn the text into JSON and take out the "articles" list
        val items = JSONObject(text).getJSONArray("articles")

        // Build one Article for each item in the list
        List(items.length()) { i ->
            val item = items.getJSONObject(i)

            Article(
                id = i,
                source = item.getJSONObject("source").getString("name"),
                // The API sometimes sends null, so use an empty string instead of crashing
                title = if (item.isNull("title")) "" else item.getString("title"),
                summary = if (item.isNull("description")) "" else item.getString("description"),
                imageUrl = if (item.isNull("urlToImage")) null
                    else item.getString("urlToImage").replace("http://", "https://") ,
                url = if (item.isNull("url")) null else item.getString("url"),
                content = if (item.isNull("content")) null else item.getString("content")
            )
        }
    } finally {
        // Always close the connection, even if something failed above
        connection.disconnect()
    }
}

// Remembers which article was tapped, so the next screen can show it
object SelectedArticle {
    var article: Article? = null
}

// Holds the user's display name. Both screens read it, and Compose redraws them when it changes.
object UserSettings {
    val displayName = mutableStateOf("")
}