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
import xyz.daijoubuteam.foodshoppingapp.model.Carousel
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.CarouselEventRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.EateryRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository
import java.util.*

class HomeViewModel: ViewModel() {
    private val eateryRepository = EateryRepository()
    private val carouselEventRepository = CarouselEventRepository()
    private val userRepository = UserRepository()
    lateinit var eateryList: LiveData<List<Eatery>>
    lateinit var popularEateryList: LiveData<List<Eatery>>
    lateinit var categoryList: LiveData<List<Category>>
    lateinit var carouselEventList: LiveData<List<Carousel>>
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

    init {
        //getEateryList()
        getPopularEateryList()
        getCategoryList()
        getCarouselList()
        if(Firebase.auth.currentUser != null) {
            getCurrentUser()
        } else {
            _currentUser = MutableLiveData();
        }
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
    private fun getEateryList() {
        val eateryListResult = eateryRepository.getListEatery()
        if(eateryListResult.isSuccess && eateryListResult.getOrNull() !== null){
            eateryList = eateryListResult.getOrNull()!!
        }else {
            onShowError(eateryListResult.exceptionOrNull()?.message)
        }
    }

    private fun getPopularEateryList() {
        getEateryList()
        popularEateryList = Transformations.map(eateryList, Function {eateryList ->
            eateryList.sortedByDescending { it.average_rating_count }
        })
    }

    private fun getCategoryList() {
        val categoryListResult = eateryRepository.getListCategory()
        if(categoryListResult.isSuccess && categoryListResult.getOrNull() !== null) {
            categoryList = categoryListResult.getOrNull()!!
        } else {
            onShowError(categoryListResult.exceptionOrNull()?.message)
        }
    }

    private fun getCarouselList() {
        val carouselListResult = carouselEventRepository.getListCarousel()
        if(carouselListResult.isSuccess && carouselListResult.getOrNull() !== null) {
            carouselEventList = carouselListResult.getOrNull()!!
        } else {
            onShowError(carouselListResult.exceptionOrNull()?.message)
        }
    }

    private fun getCurrentUser() {
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