package com.example.newshub.screens

// Holds the information for one news article
data class Article(
    val id: Int,          // unique number to identify each article
    val title: String,    // the headline
    val source: String,   // who published it
    val summary: String,   // short preview of the story
    val imageUrl: String? = null,
    val url: String? = null,
    val content: String? = null
)

// Fake articles for now, until the News API is connected
val sampleArticles = listOf(
    Article(1, "Ghana's tech scene keeps growing", "Tech Daily", "Young developers are building apps that solve local problems."),
    Article(2, "New study shows benefits of daily reading", "Science Weekly", "Researchers found that 20 minutes a day improves focus."),
    Article(3, "Local football league starts new season", "Sports Hub", "Teams are ready as the season kicks off this weekend."),
    Article(4, "Farmers adopt mobile apps for weather updates", "Agri News", "Real-time forecasts are helping farmers plan planting."),
    Article(5, "Universities expand online learning options", "Education Today", "More students can now study from anywhere.")
)