package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.Exclude

data class User(
    val uid: String?,
    val email: String?,
    val name: String?,
    @Exclude
    val isNew: Boolean? = null
)
