package xyz.daijoubuteam.foodshoppingapp.client.bag

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class BagViewModel : ViewModel(){
    private val userRepository = UserRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderList: LiveData<List<Order>>
    val orderList: LiveData<List<Order>>
        get() = _orderList
    private val _navigateToSelectedOrder = MutableLiveData<Order>()
    val navigateToSelectedOrder: LiveData<Order>
        get() = _navigateToSelectedOrder
    init {
        viewModelScope.launch {
            val orderListResult = userRepository.getOrderListInBag()
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

    val emptyCartImageVisibility = Transformations.map(orderList){
        if(it.isNullOrEmpty()){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    val orderListVisibility = Transformations.map(orderList){
        if(it.isNullOrEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }
    }

    fun doneNavigateToPlaceOrder(){
        _navigateToSelectedOrder.value = null
    }

    fun displayPlaceOrder(orderSelected: Order){
        _navigateToSelectedOrder.value = orderSelected
    }

}