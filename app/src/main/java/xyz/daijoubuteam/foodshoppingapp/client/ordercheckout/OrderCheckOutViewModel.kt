package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository
import java.time.Instant
import java.time.format.DateTimeFormatter

class OrderCheckOutViewModel(val orderId: String): ViewModel(){
    private val bagRepository = BagRepository()
    private val _errMessage = MutableLiveData("")
    private lateinit var _orderItemList: LiveData<List<BagOrderItem>>
    val orderItemList: LiveData<List<BagOrderItem>>
        get() = _orderItemList
    private var _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice
    private lateinit var _shippingAddress: LiveData<ShippingAddress?>
    val shippingAddress: LiveData<ShippingAddress?>
        get() = _shippingAddress
    private lateinit var _eatery: LiveData<Eatery>
    val eatery: LiveData<Eatery>
        get() = _eatery
//    private var _time = MutableLiveData<Timestamp>()
//    val time: LiveData<Timestamp>
//        get() = _time
    private val _navigateToOrderFragment = MutableLiveData<Boolean>()
    val navigateToOrderFragment: LiveData<Boolean>
        get() = _navigateToOrderFragment

    private val _navigateToProfileAddressEditFragment = MutableLiveData<Boolean>()
    val navigateToProfileAddressEditFragment: LiveData<Boolean>
        get() = _navigateToProfileAddressEditFragment

    private val _navigateToDetailEateryFragment = MutableLiveData<Boolean>()
    val navigateToDetailEateryFragment: LiveData<Boolean>
        get() = _navigateToDetailEateryFragment

    private val _navigateToBagOrderItemFragment = MutableLiveData<BagOrderItem>()
    val navigateToBagOrderItemFragment: LiveData<BagOrderItem>
        get() = _navigateToBagOrderItemFragment

    init {
        viewModelScope.launch {
            val orderItemResult = bagRepository.getOrderItemList(orderId)
            if(orderItemResult.isSuccess && orderItemResult.getOrNull() !== null){
                _orderItemList = orderItemResult.getOrNull()!!
            }else {
                onShowError(orderItemResult.exceptionOrNull()?.message)
            }
            val shippingAddressResult = bagRepository.getOrderAddress()
            if(shippingAddressResult.isSuccess){
                _shippingAddress = shippingAddressResult.getOrNull()!!
            }else {
                onShowError(shippingAddressResult.exceptionOrNull()?.message)
            }
            val eateryResult = bagRepository.getEateryById(orderId)
            if(eateryResult.isSuccess && eateryResult.getOrNull() !== null){
                _eatery = eateryResult.getOrNull()!!
            }else {
                onShowError(eateryResult.exceptionOrNull()?.message)
            }
        }
        _navigateToOrderFragment.value = false
        _navigateToProfileAddressEditFragment.value = false
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }

    fun totalPriceCounting(listOrder: List<BagOrderItem>){
        var sum = 0.0
        for (orderItem in listOrder){
            if(orderItem.price != null){
                sum += orderItem.price
            }
        }
        _totalPrice.value = sum
    }

    val placeOrderButtonVisible = Transformations.map(shippingAddress) {
        null != it
    }

    fun placeOrder(){
        viewModelScope.launch{
            if(!orderItemList.value.isNullOrEmpty() && shippingAddress.value != null){
                bagRepository.placeOrder(orderItemList.value!!, orderId, shippingAddress.value!!)
                _navigateToOrderFragment.value = true
            }
        }
    }

    fun doneNavigateToOrderFragment(){
        _navigateToOrderFragment.value = false
    }

    fun navigateToProfileAddressEditFragment(){
        _navigateToProfileAddressEditFragment.value = true
    }

    fun doneNavigateToProfileAddressEditFragment(){
        _navigateToProfileAddressEditFragment.value = false
    }

    fun navigateToDetailEateryFragment(){
        _navigateToDetailEateryFragment.value = true
    }

    fun doneNavigateToDetailEateryFragment(){
        _navigateToDetailEateryFragment.value = false
    }

    fun doneNavigateToBagOrderItemFragment(){
        _navigateToBagOrderItemFragment.value = null
    }

    fun navigateToBagOrderItemFragment(orderItemSelected: BagOrderItem){
        _navigateToBagOrderItemFragment.value = orderItemSelected
    }

}