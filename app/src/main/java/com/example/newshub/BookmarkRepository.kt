package com.example.newshub

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

// All Firestore code for bookmarks lives here, so screens stay simple.
object BookmarkRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Returns users/{uid}/savedArticles for the signed-in user,
    // or null if nobody is signed in.
    private fun bookmarksRef() =
        auth.currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).collection("savedArticles")
        }

    // CREATE: save an article. If it is already saved, leave it alone,
    // so saving again doesn't erase the user's note or read status.
    fun saveArticle(article: SavedArticle, onResult: (Boolean, String?) -> Unit) {
        val ref = bookmarksRef()
        if (ref == null) {
            onResult(false, "Not signed in")
            return
        }
        val doc = ref.document(article.articleId)
        doc.get()
            .addOnSuccessListener { existing ->
                if (existing.exists()) {
                    onResult(true, null) // already saved; nothing to do
                } else {
                    doc.set(article.copy(savedAt = System.currentTimeMillis()))
                        .addOnSuccessListener { onResult(true, null) }
                        .addOnFailureListener { e -> onResult(false, e.message) }
                }
            }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    // READ: listens to the user's bookmarks, newest first.
    // onChange runs now and again every time the data changes (save, update, delete).
    fun listenToBookmarks(
        onChange: (List<SavedArticle>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration? {
        val ref = bookmarksRef()
        if (ref == null) {
            onError("Not signed in")
            return null
        }
        return ref.orderBy("savedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e.message ?: "Could not load bookmarks")
                    return@addSnapshotListener
                }
                // Turn each Firestore document back into a SavedArticle object
                onChange(snapshot?.toObjects(SavedArticle::class.java) ?: emptyList())
            }
    }

    // UPDATE: mark a bookmark as read or unread
    fun setRead(articleId: String, read: Boolean, onResult: (Boolean, String?) -> Unit) {
        val ref = bookmarksRef()
        if (ref == null) {
            onResult(false, "Not signed in")
            return
        }
        ref.document(articleId)
            .update("markedRead", read)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    // UPDATE: add or edit the user's note on a bookmark
    fun setNote(articleId: String, note: String, onResult: (Boolean, String?) -> Unit) {
        val ref = bookmarksRef()
        if (ref == null) {
            onResult(false, "Not signed in")
            return
        }
        ref.document(articleId)
            .update("note", note)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    // DELETE: remove a bookmark
    fun deleteBookmark(articleId: String, onResult: (Boolean, String?) -> Unit) {
        val ref = bookmarksRef()
        if (ref == null) {
            onResult(false, "Not signed in")
            return
        }
        ref.document(articleId)
            .delete()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }
}