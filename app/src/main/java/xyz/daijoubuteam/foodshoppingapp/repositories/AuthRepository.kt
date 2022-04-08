package xyz.daijoubuteam.foodshoppingapp.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.User

class AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val usersRef = db.collection("users")

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val uid = auth.uid
            val displayName = auth.currentUser?.displayName
            val userEmail = auth.currentUser?.email
            val user = User(uid, userEmail, displayName)
            db.collection("users").add(user).await()
            Result.success(user)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<User?>{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val docs = usersRef.whereEqualTo("uid", auth.uid).get().await()
            if(docs == null || docs.isEmpty){
                throw Exception("User not found")
            }
            val user: User? = docs.firstOrNull()?.toObject(User::class.java)
            Result.success(user)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }
}