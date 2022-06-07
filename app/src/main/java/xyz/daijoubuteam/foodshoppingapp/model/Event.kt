package xyz.daijoubuteam.foodshoppingapp.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event (
    @DocumentId
    val id: String? = null,
    val name: String? = null,
    val image: String? =null,
    val content: String? = null
): Parcelable