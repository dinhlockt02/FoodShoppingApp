package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.model.Order

class OrderCheckOutViewModelFactory (
    private val orderProperty: Order,
    private val application: Application): ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(OrderCheckOutViewModel::class.java)) {
                return OrderCheckOutViewModel(orderProperty, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}