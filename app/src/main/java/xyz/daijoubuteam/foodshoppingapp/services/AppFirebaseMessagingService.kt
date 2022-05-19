package xyz.daijoubuteam.foodshoppingapp.services


import android.app.Notification
import android.app.Notification.DEFAULT_SOUND
import android.app.Notification.DEFAULT_VIBRATE
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import java.lang.Exception
import kotlin.math.roundToInt


class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val db = Firebase.firestore

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.i(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.i(message.notification?.body);

        scope.launch {

            try {
                val userId = Firebase.auth.currentUser?.uid
                if (userId.isNullOrEmpty()) return@launch
                val userRef = db.collection("users").document(userId)
                val user = userRef.get().await()
                if (!user.exists()) return@launch


                val notificationRef = userRef.collection("notifications").document()

                val notification = xyz.daijoubuteam.foodshoppingapp.model.Notification(
                    notificationRef.id,
                    message.data["title"] ?: "",
                    message.data["body"] ?: "",
                    null,
                    false,
                    message.data["sender_image_url"],
                    message.data["senderId"]
                )

                db.runBatch {
                    batch ->
                    batch.set(notificationRef, notification)
                }.addOnSuccessListener {
                    startedNotify(message)
                }


            } catch (e: Exception) {
                Timber.e(e);
                return@launch
            }
        }
    }

    private fun startedNotify(message: RemoteMessage) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.bell_outline)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["body"])
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
        with(NotificationManagerCompat.from(this))
        {
            notify(0, builder.build());
        }
    }
}