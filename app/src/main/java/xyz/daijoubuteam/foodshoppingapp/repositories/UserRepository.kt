package xyz.daijoubuteam.foodshoppingapp.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress
import xyz.daijoubuteam.foodshoppingapp.model.User

class UserRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore


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
}