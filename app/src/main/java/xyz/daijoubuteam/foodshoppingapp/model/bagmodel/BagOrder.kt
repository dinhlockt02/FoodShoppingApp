package xyz.daijoubuteam.foodshoppingapp.model.bagmodel

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude

data class BagOrder (
    val eateryId: DocumentReference?= null,
    val orderItems: ArrayList<BagOrderItem> = ArrayList(),
    @DocumentId
    val id: String?= null,
    @get:Exclude
    val eateryName: String ?= null,
    @get:Exclude
    val eateryImage: String ?= null
)