package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentReference

class OrderItem (
    val productId: DocumentReference ?= null,
    var quantity: Int?=null,
    var price: Double?=null,
)
