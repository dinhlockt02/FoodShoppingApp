package xyz.daijoubuteam.foodshoppingapp.client.orders.orderInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.repositories.OrderRepository

class OrderInformationViewModel(orderId: String): ViewModel() {
    private val orderRepository = OrderRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _order: LiveData<Order>
    val order: LiveData<Order>
        get() = _order
    private val _navigateUpToOrderFragment = MutableLiveData<Boolean>()
    val navigateUpToOrderFragment: LiveData<Boolean>
        get() = _navigateUpToOrderFragment
    init {
        viewModelScope.launch {
            val orderResult = orderRepository.getOderById(orderId)
            if(orderResult.isSuccess && orderResult.getOrNull()!==null){
                _order = orderResult.getOrNull()!!
            }else{
                onShowError(orderResult.exceptionOrNull()?.message)
            }
        }
        _navigateUpToOrderFragment.value = false
    }
    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }

    fun navigateUpToOrderFragment(){
        _navigateUpToOrderFragment.value = true
    }

    fun doneNavigateUpToOrderFragment(){
        _navigateUpToOrderFragment.value = false
    }
}