package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.app.Application
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.MainApplication
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Event
import xyz.daijoubuteam.foodshoppingapp.model.Product
import xyz.daijoubuteam.foodshoppingapp.repositories.EateryRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository
import xyz.daijoubuteam.foodshoppingapp.utils.observeOnce

class DetailEateryViewModel(eateryProperty: Eatery, app: Application): AndroidViewModel(app) {
    private val _selectedProperty = MutableLiveData<Eatery>()
    private var _distance : String = ""
    private val mainApplication = app.applicationContext as MainApplication
    private val eateryRepository = EateryRepository()
    private val userRepository = UserRepository()
    private lateinit var _productList: LiveData<List<Product>>
    lateinit var isFavorite: LiveData<Boolean>

    val selectedProperty: LiveData<Eatery>
        get() = _selectedProperty
    val distance: String
        get() = _distance
    val productList: LiveData<List<Product>>
        get() = _productList

    init {
        _selectedProperty.value = eateryProperty
        getDistanceToEatery()
        fetchProductEatery()
        getIsFavorite()
    }

    private fun getIsFavorite() {
        if(_selectedProperty.value == null) return
        val result = userRepository.isFavoritedLivedata(_selectedProperty.value!!)
        if(result.isSuccess && result.getOrNull() != null) {
            isFavorite = result.getOrNull()!!
        }
    }


    private fun getDistanceToEatery() {
        mainApplication.location.observeOnce { lct ->
            val currentLocation: Location? = lct
            val location: Location = Location(LocationManager.GPS_PROVIDER)
            selectedProperty.value?.addressEatery?.location?.let {
                location.latitude = it.latitude
                location.longitude = it.longitude
                _distance = String.format("%.2f", currentLocation?.distanceTo(location)?.div(1000)) + " km"
            }
        }
    }

    private fun fetchProductEatery() {
        Timber.i(_selectedProperty.value?.id)
        val id = _selectedProperty.value?.id
        if(id != null) {
            val productListResult = eateryRepository.getProductEatery(id)
            if (productListResult != null) {
                if(productListResult.isSuccess && productListResult.getOrNull() !== null) {
                    _productList = productListResult.getOrNull()!!
                    Timber.i(_productList.toString())
                } else {

                }

            }
        }
        else {
            return
        }
    }

    fun triggerFavorited(){
        viewModelScope.launch {
            if(_selectedProperty.value == null) return@launch
            if(isFavorite.value == true) {
                userRepository.removeFromFavorite(_selectedProperty.value!!)
            } else {
                userRepository.addToFavorite(_selectedProperty.value!!)
            }
        }
    }
}