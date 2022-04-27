package xyz.daijoubuteam.foodshoppingapp.model

data class Product (
    val id: String,
    val name: String,
    val description: String,
    val oldPrice: Int,
    val newPrice: Int,
    val imgAvatar: Int,
)
