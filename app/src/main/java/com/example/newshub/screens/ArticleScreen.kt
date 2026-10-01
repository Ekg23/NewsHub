package com.example.newshub.screens



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment      // this was the missing one
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

// @Composable means "this function draws part of the UI".
// It takes navController so it can tell the app to move to a different screen.


// The name must be exactly ArticleScreen, because MainActivity calls it by this name
@Preview(showBackground = true)
@Composable
fun ArticleScreen(navController: NavController? = null) {
    // In the preview panel show a sample article, on a device show the tapped one
    val article = if (LocalInspectionMode.current) sampleArticles.first()
    else SelectedArticle.article
    val uriHandler = LocalUriHandler.current   // opens links in the browser

    if (article == null) {
        // Safety net: nothing was selected
        Text("No article selected")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())   // lets the screen scroll if the text is long
    ) {
        // Picture at the top, only if the article has one
        article.imageUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = article.title,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.source,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )

            // NewsAPI ends its snippet with "[+1234 chars]", so cut that part off
            article.content?.substringBefore(" [+")?.let { snippet ->
                Text(
                    text = snippet,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            // Opens the full article in the browser, only if there is a link
            article.url?.let { link ->
                Button(
                    onClick = { uriHandler.openUri(link) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Read full article")
                }
            }

            Button(
                onClick = { navController?.popBackStack() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Back")
            }
        }
    }
}
@Composable
fun ArticleCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // makes the whole card tappable
    ) {
        Column {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                placeholder = ColorPainter(Color.LightGray),
                error = ColorPainter(Color.LightGray),
                fallback = ColorPainter(Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Source name in a small style
            Text(
                text = article.source,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            // Headline in a bold style
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            // Short preview, limited to 2 lines so all cards stay similar in size
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    ArticleCard(
        article = sampleArticles.first(),
        onClick = {}
    )
}