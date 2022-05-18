package xyz.daijoubuteam.foodshoppingapp.model

data class Order (
    val id: String?= null,
    val eateryId: String?= null,
    val orderItems: ArrayList<OrderItem> = ArrayList(),
    val totalPrice: Long,
)