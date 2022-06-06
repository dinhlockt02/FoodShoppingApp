package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.OrderItem
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository

class OrderCheckOutViewModel(orderId: String): ViewModel(){
    private val bagRepository = BagRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderItemList: LiveData<List<OrderItem>>
    val orderItemList: LiveData<List<OrderItem>>
        get() = _orderItemList
    private var _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice

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

    fun totalPriceCounting(listOrder: List<OrderItem>){
        var sum = 0.0
        for (orderItem in listOrder){
            if(orderItem.price != null){
                sum += orderItem.price!!
            }
        }
        _totalPrice.value = sum
    }

}