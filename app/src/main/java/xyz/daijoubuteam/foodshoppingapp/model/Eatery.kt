package xyz.daijoubuteam.foodshoppingapp.model
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Eatery (
    val id: String?=null,
    val name: String?=null,
    val address: String?=null,
    val time: Int?=null,
    val distance: Double?=null,
    val average_rating_count: Double?=null,
    val image: String?=null
): Parcelable