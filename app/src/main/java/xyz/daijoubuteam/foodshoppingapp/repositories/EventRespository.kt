package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Event
import xyz.daijoubuteam.foodshoppingapp.model.User

class EventRepository {
    private val db = Firebase.firestore

    fun fetchListEvent(): Result<LiveData<List<Event>>> {
        val events : MutableLiveData<List<Event>> = MutableLiveData()
        return try {
            val docRef = db.collection("events")
            docRef.addSnapshotListener { value, error ->
                events.value = value?.toObjects(Event::class.java)
                Timber.i(events.value.toString())
            }
            Result.success(events)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}