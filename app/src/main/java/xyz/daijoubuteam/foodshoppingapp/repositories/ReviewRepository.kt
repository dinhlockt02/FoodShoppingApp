package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.Review

class ReviewRepository {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun getRateEateryFollowUser(idEatery: String):Result<LiveData<Double>>{
        return try{
            val userId = auth.currentUser?.uid
            if(userId == null) {
                throw Exception("User not found")
            }
            val rateResult = MutableLiveData<Double>()
            db.collection("reviews").whereEqualTo("eateryId", idEatery).whereEqualTo("userId", userId).addSnapshotListener{value, error ->
                val reviews = value?.toObjects(Review::class.java)?.firstOrNull()
                if (reviews != null && reviews.rate != null) {
                    rateResult.value = reviews.rate!!
                }
            }
            Result.success(rateResult)
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }
    suspend fun updateRatingEatery(idEatery: String, rate: Double):Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
            if(userId == null) {
                throw Exception("User not found")
            }
            val checkReview = db.collection("reviews").whereEqualTo("eateryId", idEatery).whereEqualTo("userId", userId).get().await().toObjects(Review::class.java).firstOrNull()
            if(checkReview != null) {
                db.collection("reviews").document(checkReview.id!!).update("rate", rate).await()
            }
            else {
                db.collection("reviews").document().set(Review(eateryId = idEatery, rate = rate, userId = userId)).await()
            }
            calculateRating(idEatery)
            Result.success(Unit)
        }
        catch (exception: Exception) {
            Result.failure(exception)
        }
    }
    suspend fun calculateRating(idEatery: String):Result<Unit>{
        return try {
            val eateryReviewList = db.collection("reviews").whereEqualTo("eateryId", idEatery).get().await().toObjects(Review::class.java)
            if(eateryReviewList.isNullOrEmpty()) {
                Result.success(Unit)
            }
            val averRate = eateryReviewList.fold(0.0) {sum, review -> sum.plus(review.rate!!) }/eateryReviewList.size
            db.collection("eateries").document(idEatery).update("average_rating_count", averRate)
            Result.success(Unit)
        }
        catch (exception: Exception) {
            Result.failure(exception)
        }
    }
    fun checkAllowRateEatery(idEatery: String): Result<LiveData<Boolean>> {
        return try {
            val userId = auth.currentUser?.uid
            if(userId == null) {
                throw Exception("User not found")
            }
            val checkRate = MutableLiveData(false)
            val eatery = db.collection("users").document(userId).collection("orders").whereEqualTo("status", "Completed").whereEqualTo("eateryId",db.collection("eateries").document(idEatery)).addSnapshotListener{value, error ->
                if(error != null || value?.isEmpty() == true) {
                    checkRate.value = false
                }
                else {
                    checkRate.value = true
                }
            }
            Result.success(checkRate)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}