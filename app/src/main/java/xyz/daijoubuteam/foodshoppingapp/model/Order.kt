package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class Order (
    val eateryId: DocumentReference?= null,
    val orderItems: ArrayList<OrderItem> = ArrayList(),
    @DocumentId
    val id: String?= null,
    val eateryName: String ?= null,
    val eateryImage: String ?= null,
    val totalPrice: Double ?= null,
    val shippingAddress: ShippingAddress ?= null,
    val status: String = "Pending",
    val orderTime: Timestamp,
)


