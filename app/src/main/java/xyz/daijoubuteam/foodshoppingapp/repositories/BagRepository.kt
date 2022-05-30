package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

class BagRepository {
    private val db = Firebase.firestore
//    suspend fun getEatery(df: DocumentReference): Result<LiveData<Eatery>>{
//        return try {
//            val eatery: MutableLiveData<Eatery> = MutableLiveData()
//            df.addSnapshotListener{value, error ->
//                if(value!=null){
//                    eatery.value = value.toObject(Eatery::class.java)
//                }
//            }
//            Result.success(eatery)
//        }
//        catch (exception: Exception){
//            Result.failure(exception)
//        }
//    }
}