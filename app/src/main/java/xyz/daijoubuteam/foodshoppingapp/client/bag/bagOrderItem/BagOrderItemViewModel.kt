package xyz.daijoubuteam.foodshoppingapp.client.bag.bagOrderItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository

class BagOrderItemViewModel(orderId: String, productId: String): ViewModel() {
    private val bagRepository = BagRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderItem: LiveData<BagOrderItem>
    val orderItem: LiveData<BagOrderItem>
        get() = _orderItem
    init {
        viewModelScope.launch {
            val orderItemResult = bagRepository.getOrderItemByProductId(orderId, productId)
            if (orderItemResult.isSuccess) {
                _orderItem = orderItemResult.getOrNull()!!
            } else {
                onShowError(orderItemResult.exceptionOrNull()?.message)
            }
        }
    }
    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
}