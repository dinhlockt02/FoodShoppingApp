package xyz.daijoubuteam.foodshoppingapp.model


import android.os.Parcel
import android.os.Parcelable

data class User(
    val uid: String? = null,
    val email: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val isUserRegisterInformation: Boolean = false,
    val phoneNumber: String? = null,
    val isPhoneNumberVerified: Boolean = false,
    val address: ArrayList<String> = ArrayList(),
    val gender: Gender? = null,
    val nickname: String? = null
)

enum class Gender(val gender: String) {
    MALE("male"),
    FEMALE("female"),
    OTHER("other")
}