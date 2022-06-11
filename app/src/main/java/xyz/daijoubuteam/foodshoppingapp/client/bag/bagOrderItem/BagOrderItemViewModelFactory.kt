package xyz.daijoubuteam.foodshoppingapp.client.bag.bagOrderItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.client.home.product_to_bag.ProductToBagViewModel

class BagOrderItemViewModelFactory(
    private val orderId: String,
    private val productId: String,
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BagOrderItemViewModel::class.java)){
            return BagOrderItemViewModel(orderId,productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}