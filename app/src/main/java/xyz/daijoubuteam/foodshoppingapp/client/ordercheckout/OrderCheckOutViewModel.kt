package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.OrderItem
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository

class OrderCheckOutViewModel(orderId: String): ViewModel(){
    private val bagRepository = BagRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderItemList: LiveData<List<OrderItem>>
    val orderItemList: LiveData<List<OrderItem>>
        get() = _orderItemList

    init {
        viewModelScope.launch {
            val orderItemResult = bagRepository.getOrderItemList(orderId)
            if(orderItemResult.isSuccess && orderItemResult.getOrNull() !== null){
                _orderItemList = orderItemResult.getOrNull()!!
            }else {
                onShowError(orderItemResult.exceptionOrNull()?.message)
            }
        }
    }
    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
}