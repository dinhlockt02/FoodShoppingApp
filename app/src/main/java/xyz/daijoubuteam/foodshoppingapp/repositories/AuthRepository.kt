package xyz.daijoubuteam.foodshoppingapp.repositories

import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.utils.Constants
import java.sql.Time

class AuthRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun loginWithAuthCredential(authCredential: AuthCredential): Result<User?>{
        return try {
            withTimeout(Constants.DEFAULT_TIMEOUT){
                val result = auth.signInWithCredential(authCredential).await()
                val isNewUser = result.additionalUserInfo?.isNewUser
                var user: User? = null
                if(isNewUser == true){
                    user = createNewUser()
                }
                Result.success(user)
            }
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
            Result.failure(exception)
        }
    }

    suspend fun signupWithEmailAndPassword(email: String, password: String): Result<User?>{
        return try {
            withTimeout(Constants.DEFAULT_TIMEOUT){
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val isNewUser = result.additionalUserInfo?.isNewUser
                var user: User? = null
                if(isNewUser == true){
                    user = createNewUser()
                }
                auth.currentUser!!.sendEmailVerification().await()
                Result.success(user)
            }
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun resetPasswordWithEmail(email: String): Result<Boolean>?{
        return try {
            withTimeout(Constants.DEFAULT_TIMEOUT){
                auth.sendPasswordResetEmail(email).await()
                Result.success(true)
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    private suspend fun createNewUser(): User{
        return withTimeout(Constants.DEFAULT_TIMEOUT){
            val uid = auth.uid
            val userEmail = auth.currentUser?.email
            val photoUrl = auth.currentUser?.photoUrl
            val user = User(uid = uid, email = userEmail, photoUrl = photoUrl.toString())
            db.collection("users").document(user.uid!!).set(user).await()
            user;
        }
    }

    suspend fun getUserByUid(uid: String):Result<User?>{
        return try {
            withTimeout(Constants.DEFAULT_TIMEOUT){
                val docRef = db.collection("users").document(uid)
                val documentSnapShot = docRef.get().await()
                val users = documentSnapShot.toObject<User>()
                Result.success(users)
            }
        }catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun updateUserInfo(user: User): Result<Boolean>{
        return try {
            withTimeout(Constants.DEFAULT_TIMEOUT){
                db.collection("users").document(user.uid!!).set(user).await()
                Result.success(true)
            }
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }
}