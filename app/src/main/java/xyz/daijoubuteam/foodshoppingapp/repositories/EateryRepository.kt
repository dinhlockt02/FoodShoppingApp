package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Cuisine
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.User

class EateryRepository {
    private val db = Firebase.firestore

    fun getListEatery(): Result<LiveData<List<Eatery>>> {
        val eateries : MutableLiveData<List<Eatery>> = MutableLiveData()
        return try {
            val docRef = db.collection("eateries")
            docRef.addSnapshotListener { value, error ->
                eateries.value = value?.toObjects(Eatery::class.java)
            }
            Result.success(eateries)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    fun getListCategory(): Result<LiveData<List<Category>>> {
        val categories: MutableLiveData<List<Category>> = MutableLiveData()
        return try {
            val docRef = db.collection("categories")
            docRef.addSnapshotListener{value, error ->
                categories.value = value?.toObjects(Category::class.java)
            }
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}