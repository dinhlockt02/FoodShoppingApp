package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentId

data class Event (
    @DocumentId
    val id: String? = null,
    val name: String? = null,
    val image: String? =null
)