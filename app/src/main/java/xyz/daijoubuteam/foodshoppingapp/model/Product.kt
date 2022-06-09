package xyz.daijoubuteam.foodshoppingapp.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    @DocumentId
    val id: String?=null,
    val name: String?=null,
    val description: String?=null,
    val price: Double?=null,
    val img: String?=null,
    val ingredients: ArrayList<String>?=null
):Parcelable
