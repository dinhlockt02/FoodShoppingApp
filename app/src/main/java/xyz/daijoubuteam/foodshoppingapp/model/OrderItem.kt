package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class OrderItem (
    val productId: DocumentReference ?= null,
    var quantity: Int?=null,
    var price: Double?=null,
    @get:Exclude
    val productName: String?=null,
    @get:Exclude
    val productImg: String?=null
)
