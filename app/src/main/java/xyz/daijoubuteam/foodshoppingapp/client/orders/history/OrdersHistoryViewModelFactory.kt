package xyz.daijoubuteam.foodshoppingapp.client.orders.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrdersHistoryViewModelFactory: ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OrdersHistoryViewModel::class.java)){
            return OrdersHistoryViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}