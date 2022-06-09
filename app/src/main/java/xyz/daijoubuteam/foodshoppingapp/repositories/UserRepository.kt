package xyz.daijoubuteam.foodshoppingapp.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.*
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrder
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem

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
            Timber.i(user.value.toString())
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
            val notificationRef = db.collection("users").document(uid).collection("notifications")
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
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun addToFavorite(eatery: Eatery): Result<Unit> {
        return try {
            if (eatery.id.isNullOrEmpty()) throw Exception("Eatery not found.")
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val user = db.collection("users").document(uid).get().await().toObject(User::class.java)
            val eateryDocRef = db.collection("eateries").document(eatery.id)
            if (user?.favorites.isNullOrEmpty()) {
                user?.favorites = arrayListOf(eateryDocRef)
            } else {
                if(isFavorited(eatery).getOrNull() == true) throw Exception("Eatery already added in favorites")
                else {
                    user?.favorites!!.add(eateryDocRef)
                }
            }
            db.collection("users").document(uid).update("favorites", user?.favorites)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromFavorite(eatery: Eatery): Result<Unit> {
        return try {
            if (eatery.id.isNullOrEmpty()) throw Exception("Eatery not found.")
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val user = db.collection("users").document(uid).get().await().toObject(User::class.java)
            val eateryDocRef = db.collection("eateries").document(eatery.id)
            if (!user?.favorites.isNullOrEmpty()) {
                user?.favorites?.remove(eateryDocRef)
            }
            db.collection("users").document(uid).update("favorites", user?.favorites)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun isFavorited(eatery: Eatery): Result<Boolean> {
        return try {
            if (eatery.id.isNullOrEmpty()) throw Exception("Eatery not found.")
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val user = db.collection("users").document(uid).get().await().toObject(User::class.java)
            if (user?.favorites.isNullOrEmpty()) {
                Result.success(false)
            } else {
                if (user?.favorites!!.contains(
                        db.collection("eateries").document(eatery.id)
                    )
                ) Result.success(true)
                else {
                    Result.success(false)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isFavoritedLivedata(eatery: Eatery): Result<LiveData<Boolean>> {
        val isFavorited = MutableLiveData(false)
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            if (eatery.id.isNullOrEmpty()) throw Exception("Eatery not found.")
            val eateryDocRef = db.collection("eateries").document(eatery.id)
            db.collection("users").document(uid).addSnapshotListener { value, error ->
                if(value != null) {
                    val user = value.toObject(User::class.java)
                    isFavorited.value = user?.favorites?.contains(eateryDocRef)
                }
            }
            Result.success(isFavorited)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

     fun getFavoriteEateries(): Result<LiveData<List<Eatery?>>> {
        return try {
            val eateries = MutableLiveData<List<Eatery?>>(listOf())
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            var eateryListenerRegistration: ListenerRegistration? = null
            db.collection("users").document(uid).addSnapshotListener { value, error ->
                val user = value?.toObject(User::class.java)
                if(user?.favorites == null) return@addSnapshotListener
                eateryListenerRegistration?.remove()
                if(user.favorites?.isNullOrEmpty() == true){
                        eateries.value = listOf()
                } else {
                    eateryListenerRegistration = db.collection("eateries").whereIn(FieldPath.documentId(), user.favorites!!).addSnapshotListener eateriesListener@{ eateriesRef, eateriesError ->
                        if(eateriesRef == null) return@eateriesListener
                        eateries.value = eateriesRef.toObjects(Eatery::class.java)
                    }
                }

            }
           Result.success(eateries)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAddress(address: ShippingAddress): Result<Unit> {
        return try {
            if(address.defaultAddress) throw Exception("Can\'t not delete default address")
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val user = db.collection("users").document(uid).get().await().toObject(User::class.java)
            val updatedAddresses = user?.shippingAddresses?.filter {
                it.id != address.id
            }
            db.collection("users").document(uid).update("shippingAddresses", updatedAddresses).await()
            Result.success(Unit)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setDefaultAddress(address: ShippingAddress): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val user = db.collection("users").document(uid).get().await().toObject(User::class.java)
            val updatedAddresses = user?.shippingAddresses?.map {
                if(address.id == it.id) {
                    it.copy(defaultAddress = true)
                }
                else {
                    it.copy(defaultAddress = false)
                }
            }
            db.collection("users").document(uid).update("shippingAddresses", updatedAddresses).await()
            Result.success(Unit)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}