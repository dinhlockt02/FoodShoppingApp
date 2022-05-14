package xyz.daijoubuteam.foodshoppingapp.client.home

import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.withContext
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.EateryRepository
import java.util.*

class HomeViewModel: ViewModel() {
    private val eateryRepository = EateryRepository()
    lateinit var eateryList: LiveData<List<Eatery>>
    lateinit var categoryList: LiveData<List<Category>>
    private var _location: Location? = null
    private var _notification = MutableLiveData(0)
    val notification
        get() = _notification

    private val _errMessage = MutableLiveData("")
    val errMessage: LiveData<String>
        get() = _errMessage

    private val _navigateToSelectedEatery = MutableLiveData<Eatery>()
    val navigateToSelectedEatery: LiveData<Eatery>
        get() = _navigateToSelectedEatery

    init {
        getPopularEateryList()
        getCategoryList()
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }


    private fun getPopularEateryList() {
        val eateryListResult = eateryRepository.getListEatery()
        if(eateryListResult.isSuccess && eateryListResult.getOrNull() !== null){
            eateryList = eateryListResult.getOrNull()!!
        }else {
            onShowError(eateryListResult.exceptionOrNull()?.message)
        }
    }

    private fun getCategoryList() {
        val categoryListResult = eateryRepository.getListCategory()
        if(categoryListResult.isSuccess && categoryListResult.getOrNull() !== null) {
            categoryList = categoryListResult.getOrNull()!!
        } else {
            onShowError(categoryListResult.exceptionOrNull()?.message)
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