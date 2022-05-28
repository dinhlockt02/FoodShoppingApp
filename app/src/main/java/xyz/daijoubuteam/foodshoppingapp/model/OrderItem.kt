package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class OrderItem (
    val productId: DocumentReference ?= null,
    var quantity: Int?=null,
    var price: Double?=null,

):Serializable{
    @get:Exclude
    @set:Exclude
    var productName: String?=null
    @get:Exclude
    @set:Exclude
    var productImg: String?=null
}
