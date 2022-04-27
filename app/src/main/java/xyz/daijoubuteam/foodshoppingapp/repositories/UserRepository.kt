package xyz.daijoubuteam.foodshoppingapp.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress
import xyz.daijoubuteam.foodshoppingapp.model.User

class UserRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage


    suspend fun getCurrentUser():Result<User?>{
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid)
            val documentSnapShot = docRef.get().await()
            val users = documentSnapShot.toObject<User>()
            Result.success(users)

        }catch (exception: Exception){
            Result.failure(exception)
        }
    }

    fun getCurrentUserLiveData(): Result<LiveData<User>>{
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid)
            val user = MutableLiveData<User>()
            docRef.addSnapshotListener { value, error ->
                user.value = value?.toObject()
            }
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateCurrentUserInfo(user: User): Result<Boolean>{
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            db.collection("users").document(uid).set(user).await()
            Result.success(true)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun uploadAvatar(uri: Uri): Result<Uri>{
        return try {
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/${uri.lastPathSegment}")
            avatarRef.putFile(uri).await()
            val downloadUrl = avatarRef.downloadUrl.await()
            Timber.i(downloadUrl.toString())
            Result.success(downloadUrl)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}