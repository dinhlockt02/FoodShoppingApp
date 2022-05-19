package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Order (
    val eateryId: DocumentReference?= null,
    val orderItems: ArrayList<OrderItem> = ArrayList(),
    var totalPrice: Double?=null,
    val orderTime: Timestamp?=null,
    val id: String?= null,
)