package util

import dev.gitlive.firebase.firestore.QuerySnapshot

fun QuerySnapshot.isNotNullOrEmpty() = this != null
        && this.documents != null
        && this.documents.isNotEmpty()