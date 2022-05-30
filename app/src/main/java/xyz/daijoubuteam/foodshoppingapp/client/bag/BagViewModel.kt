package xyz.daijoubuteam.foodshoppingapp.client.bag

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class BagViewModel : ViewModel(){
    private val bagRepository = BagRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderList: LiveData<List<Order>>
    private val _navigateToOrderCheckOutFragment = MutableLiveData<Order>()
    val navigateToOrderCheckOutFragment: LiveData<Order>
        get() = _navigateToOrderCheckOutFragment
    val orderList: LiveData<List<Order>>
        get() = _orderList
    init {
        viewModelScope.launch {
            val orderListResult = bagRepository.getOrderListInBag()
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

    fun doneNavigateToOrderCheckOutFragment(){
        _navigateToOrderCheckOutFragment.value = null
    }

    fun navigateToOrderCheckOutFragment(orderSelected: Order){
        _navigateToOrderCheckOutFragment.value = orderSelected
    }

}