package xyz.daijoubuteam.foodshoppingapp.model

data class Eatery (
    val id: Int,
    val name: String,
    val address: String,
    val time: Int,
    val distance: Double,
    val ratingCount: Int,
    val imgRes: Int
)