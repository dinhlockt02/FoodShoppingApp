package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

class OrderItem (
    val productId: DocumentReference ?= null,
    var quantity: Int?=null,
    var price: Double?=null,
): Serializable {

}
