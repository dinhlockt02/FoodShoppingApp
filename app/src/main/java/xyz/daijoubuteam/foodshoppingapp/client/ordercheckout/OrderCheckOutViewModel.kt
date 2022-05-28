package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class OrderCheckOutViewModel(orderProperty: Order, app: Application): AndroidViewModel(app) {
    private val userRepository = UserRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _selectedProperty: LiveData<Order>
    val selectedProperty: LiveData<Order>
        get() = _selectedProperty
    init {
        viewModelScope.launch {
            val orderResult = userRepository.placeOrder(orderProperty)
            if(orderResult.isSuccess && orderResult.getOrNull() !== null){
                _selectedProperty = orderResult.getOrNull()!!
            }else {
                onShowError(orderResult.exceptionOrNull()?.message)
            }
        }
    }
    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
}