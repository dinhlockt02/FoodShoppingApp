package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.firestore.DocumentReference
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem

data class OrderItem (
    var productId: DocumentReference?= null,
    var quantity: Int?=null,
    var price: Double?=null,
    var productPrice: Double?=null,
    var productName: String?=null,
    var productImg: String?=null
)

