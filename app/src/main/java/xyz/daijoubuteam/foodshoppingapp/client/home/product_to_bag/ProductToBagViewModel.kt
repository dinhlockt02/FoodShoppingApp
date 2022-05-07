package xyz.daijoubuteam.foodshoppingapp.client.home.product_to_bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.daijoubuteam.foodshoppingapp.model.Product

class ProductToBagViewModel(product: Product, app: Application): AndroidViewModel(app) {
    private val _selectedProperty = MutableLiveData<Product>()
    val selectedProperty: LiveData<Product>
        get() = _selectedProperty
    init {
        _selectedProperty.value = product
    }
}