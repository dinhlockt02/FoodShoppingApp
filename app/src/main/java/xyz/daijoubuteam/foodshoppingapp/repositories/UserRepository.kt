package xyz.daijoubuteam.foodshoppingapp.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Notification
import xyz.daijoubuteam.foodshoppingapp.model.OrderItem
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress
import xyz.daijoubuteam.foodshoppingapp.model.User

class UserRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid)
            val documentSnapShot = docRef.get().await()
            val users = documentSnapShot.toObject<User>()
            Result.success(users)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getCurrentUserLiveData(): Result<LiveData<User>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid)
            val user = MutableLiveData<User>()
            docRef.addSnapshotListener { value, error ->
                user.value = value?.toObject()
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCurrentUserInfo(user: User): Result<Boolean> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            db.collection("users").document(uid).set(user).await()
            Result.success(true)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun uploadAvatar(uri: Uri): Result<Uri> {
        return try {
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/${uri.lastPathSegment}")
            avatarRef.putFile(uri).await()
            val downloadUrl = avatarRef.downloadUrl.await()
            Timber.i(downloadUrl.toString())
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserNotificationLiveData(): Result<LiveData<List<Notification>>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val notificationRef = db.collection("users").document(uid).collection("notifications").orderBy("timestampFirebase", Query.Direction.DESCENDING)
            val notifications = MutableLiveData<List<Notification>>()
            notificationRef.addSnapshotListener { value, error ->
                val notificationsList = value?.toObjects(Notification::class.java)
                notifications.value = notificationsList ?: listOf()
                Timber.i(notifications.value?.size.toString())
            }
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotification(notification: Notification): Result<Boolean> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            if (notification.id == null) throw Exception("Notification not found.")
            db.collection("users").document(uid).collection("notifications").document(
                notification.id!!
            ).set(notification).await()
            Result.success(true);
        }catch (exception: Exception){
            Result.failure(exception)
        }
    }
//    fun getListOrderItemByEateryId(): Result<LiveData<List<OrderItem>>>{
//        return try {
//            val uid = auth.currentUser?.uid
//                ?: throw Exception("Current user not found.")
//            val docRef = db.collection("users").document(uid)
//            val orderItemList: MutableLiveData<List<OrderItem>> = MutableLiveData()
//            val user = MutableLiveData<User>()
//            docRef.addSnapshotListener { value, error ->
//                user.value = value?.toObject()
//            }
//            orderItemList.value =  user.value?.bag
//            orderItemList.value?.find { orderItem -> orderItem.productId?.contains("c8vy6QVL2ZTLC0uOrdV7") ?: false }
//            Timber.i(orderItemList.toString())
//            Result.success(orderItemList)
//        } catch (exception: Exception){
//            Result.failure(exception)
//        }
//    }
}