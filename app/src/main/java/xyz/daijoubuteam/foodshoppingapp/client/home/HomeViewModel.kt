package xyz.daijoubuteam.foodshoppingapp.client.home

import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Event
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.EateryRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.EventRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository
import java.util.*


enum class TypesViewAll {
    POPULAR,
    NEARBY
}

class HomeViewModel: ViewModel() {
    private val eateryRepository = EateryRepository()
    private val userRepository = UserRepository()
    private val eventRepository = EventRepository()
    lateinit var eateryList: LiveData<List<Eatery>>
    lateinit var popularEateryList: LiveData<List<Eatery>>
    lateinit var categoryList: LiveData<List<Category>>
    private lateinit var _eventList: LiveData<List<Event>>
    private lateinit var _currentUser: LiveData<User>
    private var _location: Location? = null
    private var _notification = MutableLiveData(0)
    private val _errMessage = MutableLiveData("")
    private val _navigateToSelectedEatery = MutableLiveData<Eatery>()
    val notification
        get() = _notification
    val errMessage: LiveData<String>
        get() = _errMessage
    val navigateToSelectedEatery: LiveData<Eatery>
        get() = _navigateToSelectedEatery
    val currentUser: LiveData<User>
        get() = _currentUser
    val eventList: LiveData<List<Event>>
        get()= _eventList


    init {
        fetchEventList()
        fetchPopularEateryList()
        fetchCategoryList()
        //getCarouselList()
        if(Firebase.auth.currentUser != null) {
            fetchCurrentUser()
        } else {
            _currentUser = MutableLiveData();
        }
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }

    private fun fetchEventList() {
        val eventListResult = eventRepository.fetchListEvent()
        if(eventListResult.isSuccess && eventListResult.getOrNull() !== null){
            _eventList = eventListResult.getOrNull()!!
        }else {
            onShowError(eventListResult.exceptionOrNull()?.message)
        }
    }

    private fun fetchEateryList() {
        val eateryListResult = eateryRepository.getListEatery()
        if(eateryListResult.isSuccess && eateryListResult.getOrNull() !== null){
            eateryList = eateryListResult.getOrNull()!!
        }else {
            onShowError(eateryListResult.exceptionOrNull()?.message)
        }
    }

    private fun fetchPopularEateryList() {
        fetchEateryList()
        popularEateryList = Transformations.map(eateryList, Function {eateryList ->
            eateryList.sortedByDescending { it.average_rating_count }
        })
    }

    private fun fetchCategoryList() {
        val categoryListResult = eateryRepository.getListCategory()
        if(categoryListResult.isSuccess && categoryListResult.getOrNull() !== null) {
            categoryList = categoryListResult.getOrNull()!!
        } else {
            onShowError(categoryListResult.exceptionOrNull()?.message)
        }
    }

    private fun fetchCurrentUser() {
        val currentUserDB =  userRepository.getCurrentUserLiveData()
        if(currentUserDB.isSuccess && currentUserDB.getOrNull() != null) {
            _currentUser = currentUserDB.getOrNull()!!
        } else {
            onShowError(currentUserDB.exceptionOrNull()?.message)
        }
    }

    fun onNavigateToSelectedEateryComplete(){
        _navigateToSelectedEatery.value = null
    }

    //show detail eatery selected
    fun displayPropertyDetailEatery(eaterySelected: Eatery) {
        _navigateToSelectedEatery.value = eaterySelected
    }

}