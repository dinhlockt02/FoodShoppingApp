package xyz.daijoubuteam.foodshoppingapp.client.bag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun initOrder(){
        userRepository.initOrderInBag()
    }
}