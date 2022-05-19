package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.app.Application
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.daijoubuteam.foodshoppingapp.MainApplication
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.utils.observeOnce

class DetailEateryViewModel(eateryProperty: Eatery, app: Application): AndroidViewModel(app) {
    private val _selectedProperty = MutableLiveData<Eatery>()
    private var _distance : String = ""
    private val mainApplication = app.applicationContext as MainApplication

    val selectedProperty: LiveData<Eatery>
        get() = _selectedProperty
    val distance: String
        get() = _distance

    init {
        _selectedProperty.value = eateryProperty
        getDistanceToEatery()
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
}