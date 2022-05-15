package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Carousel
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

class CarouselEventRepository {
    private val db = Firebase.firestore

    fun getListCarousel(): Result<LiveData<List<Carousel>>> {
        val carouselList : MutableLiveData<List<Carousel>> = MutableLiveData()
        return try {
            val docRef = db.collection("carouselEvent")
            docRef.addSnapshotListener { value, error ->
                carouselList.value = value?.toObjects(Carousel::class.java)
            }
            Result.success(carouselList)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}