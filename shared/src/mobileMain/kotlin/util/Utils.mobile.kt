package util

import dev.gitlive.firebase.firestore.QuerySnapshot
import library.MR

fun QuerySnapshot.isNotNullOrEmpty() = this != null
        && this.documents != null
        && this.documents.isNotEmpty()

expect fun geminiApiKey(): String

expect interface GeminiInterface {
    val apiKey: String
}

object ResHolder {
    fun getModelFile(): MR.files {
        return MR.files
    }
}