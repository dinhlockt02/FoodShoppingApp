package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

class CategoryRepository {
    private val db = Firebase.firestore

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

    fun getListEateryByCategory(typeEatery: String): Result<LiveData<List<Eatery>>> {
        val eateries: MutableLiveData<List<Eatery>> = MutableLiveData()
        return try {
            val docRef = db.collection("eateries")
            docRef.addSnapshotListener{ value, error ->
               val eateryList = value?.toObjects(Eatery::class.java)
                eateries.value = eateryList?.filter { eatery -> eatery.type == typeEatery  }
            }
            Result.success(eateries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}