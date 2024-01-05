package util

import dev.gitlive.firebase.firestore.QuerySnapshot
import models.CategoryOfGamelan
import models.Gamelan

fun QuerySnapshot.isNotNullOrEmpty() = this != null
        && this.documents != null
        && this.documents.isNotEmpty()