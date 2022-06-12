package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ListenerRegistration
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

    fun fetchEateryFollowEvent(id: String): Result<LiveData<List<Eatery?>>> {
        return try {
            val eateries = MutableLiveData<List<Eatery?>>(listOf())
            var eateryListenerRegistration: ListenerRegistration? = null
            db.collection("events").document(id).addSnapshotListener {value, error ->
                val eventEateries = value?.get("idEateryList") as? ArrayList<DocumentReference>
                if(eventEateries == null) return@addSnapshotListener
                eateryListenerRegistration?.remove()
                if(eventEateries.isNullOrEmpty() == true) {
                    eateries.value = listOf()
                }
                else {
                    eateryListenerRegistration = db.collection("eateries").whereIn(FieldPath.documentId(), eventEateries).addSnapshotListener{ eateryRef, eateryError ->
                        if(eateryRef == null) return@addSnapshotListener
                        eateries.value = eateryRef.toObjects(Eatery::class.java)

                    }
                }
            }
            Result.success(eateries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}