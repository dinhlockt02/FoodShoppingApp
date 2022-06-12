package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

class Review (
    @DocumentId
    val id: String?=null,
    val eateryId: String? = null,
    val userId: String? = null,
    val rate: Double? = null
)