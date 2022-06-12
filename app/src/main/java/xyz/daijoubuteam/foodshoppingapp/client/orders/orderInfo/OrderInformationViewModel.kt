package xyz.daijoubuteam.foodshoppingapp.client.orders.orderInfo

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.repositories.OrderRepository

class OrderInformationViewModel(orderId: String): ViewModel() {
    private val orderRepository = OrderRepository()
    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message
    private lateinit var _order: LiveData<Order>
    val order: LiveData<Order>
        get() = _order
    private lateinit var _eatery: LiveData<Eatery>
    val eatery: LiveData<Eatery>
        get() = _eatery
    private val _navigateUpToOrderFragment = MutableLiveData<Boolean>()
    val navigateUpToOrderFragment: LiveData<Boolean>
        get() = _navigateUpToOrderFragment
    private val _navigateToEateryDetailFragment = MutableLiveData<Boolean>()
    val navigateToEateryDetailFragment: LiveData<Boolean>
        get() = _navigateToEateryDetailFragment

    init {
        viewModelScope.launch {
            val orderResult = orderRepository.getOrderById(orderId)
            if(orderResult.isSuccess && orderResult.getOrNull()!==null){
                _order = orderResult.getOrNull()!!
            }else{
                onShowMessage(orderResult.exceptionOrNull()?.message)
            }
            val eateryResult = orderRepository.getEateryByOrderId(orderId)
            if(eateryResult.isSuccess && eateryResult.getOrNull()!==null){
                _eatery = eateryResult.getOrNull()!!
            }else{
                onShowMessage(eateryResult.exceptionOrNull()?.message)
            }
        }
        _navigateUpToOrderFragment.value = false
        _navigateToEateryDetailFragment.value = false
    }
    private fun onShowMessage(msg: String?){
        this._message.value = msg
    }

    fun navigateUpToOrderFragment(){
        _navigateUpToOrderFragment.value = true
    }

    fun doneNavigateUpToOrderFragment(){
        _navigateUpToOrderFragment.value = false
    }

    val ratingTextViewVisibility = Transformations.map(order){
        if(it.status == "Completed"){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    fun navigateToEateryDetailFragment(){
        _navigateToEateryDetailFragment.value = true
    }

    fun doneNavigateToEateryDetailFragment(){
        _navigateToEateryDetailFragment.value = false
    }

    fun messageCantFindEatery(){
        onShowMessage("Can't find this eatery")
    }

    fun onShowMessageComplete() {
        _message.value = ""
    }


}