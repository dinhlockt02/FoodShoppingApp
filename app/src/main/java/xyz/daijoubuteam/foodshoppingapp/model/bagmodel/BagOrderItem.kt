package xyz.daijoubuteam.foodshoppingapp.model.bagmodel

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import xyz.daijoubuteam.foodshoppingapp.model.OrderItem
import java.io.Serializable

data class BagOrderItem (
    val productId: DocumentReference ?= null,
    var quantity: Int?=null,
    @get:Exclude
    val price: Double?=null,
    @get:Exclude
    val productPrice: Double?=null,
    @get:Exclude
    val productName: String?=null,
    @get:Exclude
    val productImg: String?=null
)
{
    fun toOrderItem() = OrderItem (
        productId = productId,
        quantity = quantity,
        price = price,
        productPrice = productPrice,
        productName = productName,
        productImg = productImg
    )
}
