package xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrder
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.OrderRepository

class OrdersOnGoingViewModel: ViewModel() {
    private val orderRepository = OrderRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderList: LiveData<List<Order>>
    private val _navigateToOrderInfoFragment = MutableLiveData<Order>()
    val navigateToOrderInfoFragment: LiveData<Order>
        get() = _navigateToOrderInfoFragment
    val orderList: LiveData<List<Order>>
        get() = _orderList

    init {
        viewModelScope.launch {
            val orderListResult = orderRepository.getOrderOnGoing()
            if(orderListResult.isSuccess && orderListResult.getOrNull() !== null){
                _orderList = orderListResult.getOrNull()!!
            }else {
                onShowError(orderListResult.exceptionOrNull()?.message)
            }
        }
    }

    val emptyImageVisibility: LiveData<Int> = Transformations.map(orderList){
        if(it.isNullOrEmpty()){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }

    fun doneNavigateToOrderInfoFragment(){
        _navigateToOrderInfoFragment.value = null
    }

    fun navigateToOrderInfoFragment(orderSelected: Order){
        _navigateToOrderInfoFragment.value = orderSelected
    }
}