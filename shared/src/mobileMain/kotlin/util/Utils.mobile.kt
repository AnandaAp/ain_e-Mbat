package util

import dev.gitlive.firebase.firestore.QuerySnapshot

fun QuerySnapshot.isNotNullOrEmpty() = this != null
        && this.documents != null
        && this.documents.isNotEmpty()

expect fun geminiApiKey(): String

expect interface GeminiInterface {
    val apiKey: String
}