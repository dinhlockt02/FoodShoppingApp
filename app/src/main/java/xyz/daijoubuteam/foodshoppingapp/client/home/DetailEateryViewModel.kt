package xyz.daijoubuteam.foodshoppingapp.client.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

class DetailEateryViewModel(eateryProperty: Eatery, app: Application): AndroidViewModel(app) {
    private val _selectedProperty = MutableLiveData<Eatery>()
    val selectedProperty: LiveData<Eatery>
        get() = _selectedProperty

    init {
        _selectedProperty.value = eateryProperty
    }
}