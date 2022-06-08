package xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.client.bag.BagViewModel

class OrdersOnGoingViewModelFactory: ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OrdersOnGoingViewModel::class.java)){
            return OrdersOnGoingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}