package xyz.daijoubuteam.foodshoppingapp.model


import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentReference
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem

data class User(
    val uid: String? = null,
    val email: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var isUserRegisterInformation: Boolean = false,
    var phoneNumber: String? = null,
    var isPhoneNumberVerified: Boolean = false,
    val shippingAddresses: ArrayList<ShippingAddress> = ArrayList(),
    var gender: Gender? = null,
    var nickname: String? = null,
    var photoUrl: String? = null,
    var favorites: ArrayList<DocumentReference>? = null
    var bag: ArrayList<BagOrderItem> = ArrayList()
)

