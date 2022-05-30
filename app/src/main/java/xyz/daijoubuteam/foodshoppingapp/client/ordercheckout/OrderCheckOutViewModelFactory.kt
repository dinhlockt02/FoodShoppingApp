package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderCheckOutViewModelFactory (
    private val orderId: String): ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(OrderCheckOutViewModel::class.java)) {
                return OrderCheckOutViewModel(orderId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}