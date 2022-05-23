package xyz.daijoubuteam.foodshoppingapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import timber.log.Timber
import java.io.Serializable

data class Order (
    val eateryId: DocumentReference?= null,
    val orderItems: ArrayList<OrderItem> = ArrayList(),
    var totalPrice: Double?=null,
    val orderTime: Timestamp?=null,
    @DocumentId
    val id: String?= null,
):Serializable{


    @get:Exclude
    @set:Exclude
    var eatery: Eatery? = Eatery()
}