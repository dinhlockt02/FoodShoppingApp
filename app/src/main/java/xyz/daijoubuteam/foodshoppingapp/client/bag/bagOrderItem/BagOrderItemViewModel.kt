package xyz.daijoubuteam.foodshoppingapp.client.bag.bagOrderItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Product
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository

class BagOrderItemViewModel(orderId: String, productId: String, quantity: Int): ViewModel() {
    private val bagRepository = BagRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _product: LiveData<Product>
    val product: LiveData<Product>
        get() = _product
    private val _orderQuantity = MutableLiveData<Int>()
    val orderQuantity: LiveData<Int>
        get() = _orderQuantity
    private val _totalPrice=  MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double>
        get() = _totalPrice
    init {
        viewModelScope.launch {
            val orderItemResult = bagRepository.getProductByProductId(productId)
            if (orderItemResult.isSuccess) {
                _product = orderItemResult.getOrNull()!!
            } else {
                onShowError(orderItemResult.exceptionOrNull()?.message)
            }
        }
        _orderQuantity.value = quantity
        setTotalPrice()
    }
    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }

    fun setTotalPrice() {
        _totalPrice.value = _product.value?.price?.let { _orderQuantity.value?.times(it) }
    }
}