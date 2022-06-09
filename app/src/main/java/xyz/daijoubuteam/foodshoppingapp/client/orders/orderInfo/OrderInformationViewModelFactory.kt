package xyz.daijoubuteam.foodshoppingapp.client.orders.orderInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class OrderInformationViewModelFactory (
    private val orderId: String
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OrderInformationViewModel::class.java)) {
            return OrderInformationViewModel(orderId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}