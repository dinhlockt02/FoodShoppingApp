package xyz.daijoubuteam.foodshoppingapp.client.bag.bagOrderItem

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Product
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository

class BagOrderItemViewModel(orderId: String, productId: String, quantity: Int): ViewModel() {
    private val bagRepository = BagRepository()
    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message
    private lateinit var _product: LiveData<Product>
    val product: LiveData<Product>
        get() = _product
    private val _orderQuantity = MutableLiveData<Int>()
    val orderQuantity: LiveData<Int>
        get() = _orderQuantity
    private val _bagOrderId = MutableLiveData<String>()
    val bagOrderId: LiveData<String>
        get() = _bagOrderId
    private val _productIdRef = MutableLiveData<String>()
    val productIdRef: LiveData<String>
        get() = _productIdRef
    private val _totalPrice=  MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double>
        get() = _totalPrice
    private val _navigateUpToOrderCheckOutFragment = MutableLiveData<Boolean>()
    val navigateUpToOrderCheckOutFragment: LiveData<Boolean>
        get() = _navigateUpToOrderCheckOutFragment

    init {
        viewModelScope.launch {
            val orderItemResult = bagRepository.getProductByProductId(productId)
            if (orderItemResult.isSuccess) {
                _product = orderItemResult.getOrNull()!!
            } else {
                onShowMessage(orderItemResult.exceptionOrNull()?.message)
            }
        }
        _orderQuantity.value = quantity
        _bagOrderId.value = orderId
        _productIdRef.value = productId
        _navigateUpToOrderCheckOutFragment.value = false
        setTotalPrice()
    }
    private fun onShowMessage(msg: String?){
        this._message.value = msg
    }

    fun setTotalPrice() {
        _totalPrice.value = _product.value?.price?.let { _orderQuantity.value?.times(it) }
    }

    fun raiseQuantityItem(){
        _orderQuantity.value = _orderQuantity.value?.plus(1)
    }

    fun reduceQuantityItem(){
        _orderQuantity.value = _orderQuantity.value?.minus(1)
    }

    val minusButtonVisible = Transformations.map(orderQuantity){
        0 != it
    }

    fun saveOrderItemChange(){
        viewModelScope.launch {
            if(_orderQuantity.value != null && _bagOrderId.value != null && _productIdRef.value != null){
                val res = bagRepository.saveOrderItemChange(_bagOrderId.value!!, _productIdRef.value!!, _orderQuantity.value!!)
                if(res.isFailure) {
                    onShowMessage("Failed")
                } else if (_orderQuantity.value == 0){
                    onShowMessage("This order item has been deleted")
                }else{
                    onShowMessage("This order item has been changed")
                }
                navigateUpToOrderCheckOutFragment()
            }
        }
    }

    fun navigateUpToOrderCheckOutFragment(){
        _navigateUpToOrderCheckOutFragment.value = true
    }

    fun doneNavigateUpToOrderCheckOutFragment(){
        _navigateUpToOrderCheckOutFragment.value = false
    }

    fun showMessageForNullProduct(){
        _message.value = "This product is no longer exist"
    }

    fun onShowMessageComplete() {
        _message.value = ""
    }
}