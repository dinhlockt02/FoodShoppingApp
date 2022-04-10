package xyz.daijoubuteam.foodshoppingapp.repositories

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.User

class AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun loginWithAuthCredential(authCredential: AuthCredential): Result<FirebaseUser?>{
        return try {
            val result = auth.signInWithCredential(authCredential).await()
            val isNewUser = result.additionalUserInfo?.isNewUser
            if(isNewUser == true){
                createNewUser()
            }
            Result.success(auth.currentUser)
        }
        catch (firebaseAuthUserCollisionException: FirebaseAuthUserCollisionException){
            val email = firebaseAuthUserCollisionException.email
            val exception = if(email !== null) {
                val result = auth.fetchSignInMethodsForEmail(email).await()
                val method = result.signInMethods?.firstOrNull()
                Exception("Email has already been used. Please try login with $method")
            } else {
                Exception("Email has already been used.")
            }
            Result.failure(exception)
        }
        catch (exception: Exception){
            Log.e("login", exception.message, exception)
            Result.failure(exception)
        }
    }

    suspend fun signupWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>{
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
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