package xyz.daijoubuteam.foodshoppingapp.client.orders.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingViewModel

class OrdersUpComingViewModelFactory : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OrdersUpComingViewModel::class.java)){
            return OrdersUpComingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}