package xyz.daijoubuteam.foodshoppingapp.model


import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class User(
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null,
    @Exclude
    val isNew: Boolean? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeValue(isNew)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
