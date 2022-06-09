package xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrder
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.OrderRepository

class OrdersOnGoingViewModel: ViewModel() {
    private val orderRepository = OrderRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderList: LiveData<List<Order>>
//    private val _navigateToOrderCheckOutFragment = MutableLiveData<Order>()
//    val navigateToOrderCheckOutFragment: LiveData<Order>
//        get() = _navigateToOrderCheckOutFragment
    val orderList: LiveData<List<Order>>
        get() = _orderList

    init {
        viewModelScope.launch {
            val orderListResult = orderRepository.getOrderUpComing()
            if(orderListResult.isSuccess && orderListResult.getOrNull() !== null){
                _orderList = orderListResult.getOrNull()!!
            }else {
                onShowError(orderListResult.exceptionOrNull()?.message)
            }
        }
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
}