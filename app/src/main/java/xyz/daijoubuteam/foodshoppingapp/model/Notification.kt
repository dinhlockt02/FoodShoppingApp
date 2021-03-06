package xyz.daijoubuteam.foodshoppingapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Notification(
    @DocumentId
    @Exclude
    var id: String? = null,
    val title: String? = null ,
    val body: String? = null,
    @ServerTimestamp
    val timestampFirebase: Date? = null,
    val notificationRead: Boolean = false,
) {
}
