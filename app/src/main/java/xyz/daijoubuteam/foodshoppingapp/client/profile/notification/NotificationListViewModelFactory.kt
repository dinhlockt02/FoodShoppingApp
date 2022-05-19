package xyz.daijoubuteam.foodshoppingapp.client.profile.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.client.profile.ProfileViewModel

class NotificationListViewModelFactory(): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotificationListViewModel::class.java)){
            return NotificationListViewModel() as T
        }
        throw IllegalArgumentException("Unknown NotificationListViewModel class")
    }
}