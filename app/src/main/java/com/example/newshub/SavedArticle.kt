package com.example.newshub

data class SavedArticle (
    val articleId: String = "",   // also used as the document ID, so saving twice won't duplicate
    val title: String = "",
    val url: String = "",
    val source: String = "",
    val imageUrl: String = "",
    val savedAt: Long = 0L,       // time saved, in milliseconds
    val markedRead: Boolean = false, // UPDATE: has the user marked this as read?
    val note: String = ""         // used later for the Update operation
)