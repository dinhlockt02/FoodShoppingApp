package xyz.daijoubuteam.foodshoppingapp.client.home.product_to_bag

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery.DetailEateryViewModel
import xyz.daijoubuteam.foodshoppingapp.model.Product

class ProductToBagViewModelFactory(
    private val eateryId: String,
    private val product: Product,
    private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProductToBagViewModel::class.java)){
            return ProductToBagViewModel(eateryId,product,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}