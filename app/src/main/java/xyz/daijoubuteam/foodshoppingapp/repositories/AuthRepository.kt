package xyz.daijoubuteam.foodshoppingapp.repositories

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.User

class AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val usersRef = db.collection("users")

    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            createNewUser()
            Result.success(auth.currentUser)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun loginWithGoogle(googleAuthCredential: AuthCredential): Result<FirebaseUser?>{
        return try {
            val result = auth.signInWithCredential(googleAuthCredential).await()
            val isNewUser = result.additionalUserInfo?.isNewUser
            if(isNewUser == true){
                createNewUser()
            }
            Result.success(auth.currentUser)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    private suspend fun createNewUser(){
        val uid = auth.uid
        val displayName = auth.currentUser?.displayName
        val userEmail = auth.currentUser?.email
        val user = User(uid, userEmail, displayName)
        db.collection("users").add(user).await()
    }
}