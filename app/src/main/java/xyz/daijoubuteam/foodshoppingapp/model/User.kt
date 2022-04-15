package xyz.daijoubuteam.foodshoppingapp.model


import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class User(
    val uid: String? = null,
    val email: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var isUserRegisterInformation: Boolean = false,
    var phoneNumber: String? = null,
    var isPhoneNumberVerified: Boolean = false,
    val address: ArrayList<String> = ArrayList(),
    var gender: Gender? = null,
    var nickname: String? = null,
    var photoUrl: Uri? = null
)

enum class Gender(val gender: String) {
    MALE("male"),
    FEMALE("female"),
    OTHER("other")
}