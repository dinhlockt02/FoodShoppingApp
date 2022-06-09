package xyz.daijoubuteam.foodshoppingapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    val id: String?=null,
    val name: String?=null,
    val description: String?=null,
    val price: Double?=null,
    val img: String?=null,
    val ingredients: ArrayList<String>?=null
):Parcelable
