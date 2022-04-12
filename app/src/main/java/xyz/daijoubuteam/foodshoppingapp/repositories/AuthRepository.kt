package xyz.daijoubuteam.foodshoppingapp.repositories

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.User

class AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun loginWithAuthCredential(authCredential: AuthCredential): Result<User?>{
        return try {
            val result = auth.signInWithCredential(authCredential).await()
            val isNewUser = result.additionalUserInfo?.isNewUser
            var user: User? = null
            if(isNewUser == true){
                user = createNewUser()
            }
            Result.success(user)
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

    suspend fun signupWithEmailAndPassword(email: String, password: String): Result<User?>{
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val isNewUser = result.additionalUserInfo?.isNewUser
            var user: User? = null
            if(isNewUser == true){
                user = createNewUser()
            }
            auth.currentUser!!.sendEmailVerification().await()
            Result.success(user)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun resetPasswordWithEmail(email: String): Result<Boolean>?{
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    private suspend fun createNewUser(): User{
        val uid = auth.uid
        val userEmail = auth.currentUser?.email
        val user = User(uid = uid, email = userEmail)
        db.collection("users").document(user.uid!!).set(user).await()
        return user
    }

    suspend fun getUserByUid(uid: String):Result<User?>{
        return try {
            val docRef = db.collection("users").document(uid)
            val documentSnapShot = docRef.get().await()
            val users = documentSnapShot.toObject<User>()
            Result.success(users)

        }catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun updateUserInfo(user: User): Result<Boolean>{
        return try {
            db.collection("users").document(user.uid!!).set(user).await()
            Result.success(true)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }
}