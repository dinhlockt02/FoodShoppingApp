package xyz.daijoubuteam.foodshoppingapp.client.home.product_to_bag

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Product
import xyz.daijoubuteam.foodshoppingapp.repositories.BagRepository

class ProductToBagViewModel(eateryId: String, product: Product, app: Application): AndroidViewModel(app) {
    private val _selectedProperty = MutableLiveData<Product>()
    private val _eateryId = MutableLiveData<String>()
    private val _quantityItem = MutableLiveData<Int>(0)
    private val _totalPrice=  MutableLiveData<Number>(0)
    private val bagRepository = BagRepository()
    private val _message = MutableLiveData("")

    val selectedProperty: LiveData<Product>
        get() = _selectedProperty

    val quantityItem: LiveData<Int>
        get() = _quantityItem
    val totalPrice: LiveData<Number>
        get() = _totalPrice

    val message: LiveData<String>
    get() = _message

    init {
        _selectedProperty.value = product
        _eateryId.value = eateryId
    }

    fun handleRaiseQuantityItem() {
        _quantityItem.value = _quantityItem.value?.plus(1)
        setTotalPrice()
    }

    fun handleMinusQuantityItem() {
        if(_quantityItem.value!! <=0 ) return
        _quantityItem.value = _quantityItem.value?.minus(1)
        setTotalPrice()
    }

    fun setTotalPrice() {
        _totalPrice.value = selectedProperty.value?.price?.let { _quantityItem.value?.times(it) }
    }

    fun onShowMessage(msg: String?) {
        this._message.value  = msg
    }

    val buttonVisibility = Transformations.map(quantityItem) {
        _selectedProperty.value?.id != null && quantityItem.value != null && quantityItem.value!! > 0
    }

    fun handleBtnAddToBag() {
        viewModelScope.launch {
            if(_selectedProperty.value?.id != null && quantityItem.value != null && quantityItem.value!! > 0) {
                val res = bagRepository.addNewOderItem(_eateryId.value!!,_selectedProperty.value!!.id!!, _quantityItem.value!!)
                if(res.isFailure) {
                    onShowMessage("Failed")
                } else {
                    onShowMessage("Successful")
                }
            }
        }
    }

    fun onShowMessageComplete() {
        _message.value = ""
    }
}