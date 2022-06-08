package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import timber.log.Timber
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
//    private var _time = MutableLiveData<Timestamp>()
//    val time: LiveData<Timestamp>
//        get() = _time

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
        }
        val a = Timestamp(System.currentTimeMillis()/1000, 0)
        Timber.i(a.toDate().toString())
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
            }
        }
    }

}