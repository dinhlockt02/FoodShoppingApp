package xyz.daijoubuteam.foodshoppingapp.model
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint

@Parcelize
data class Eatery (
    @DocumentId
    val id: String?=null,
    val name: String?=null,
    val work_time: String?=null,
    val average_rating_count: Double?=null,
    val description: String?=null,
    val products: ArrayList<Product>?=null,
    val photoUrl: String?=null,
    val addressEatery: EateryAddress? = null,
    val type: String? = null,
    @Exclude
    val distance: Float = Float.MAX_VALUE
): Parcelable