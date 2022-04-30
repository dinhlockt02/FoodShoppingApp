package xyz.daijoubuteam.foodshoppingapp.model
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Eatery (
    val id: String?=null,
    val name: String?=null,
    val address: String?=null,
    val work_time: String?=null,
    val average_rating_count: Double?=null,
    val description: String?=null,
    val reviews: ArrayList<String>?=null,
    val products: ArrayList<Product>?=null,
    val image: String?=null
): Parcelable