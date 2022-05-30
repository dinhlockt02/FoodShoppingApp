package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentId

data class Category (
    @DocumentId
    val id: String?=null,
    val name: String?=null,
    val icon: String?=null
)
