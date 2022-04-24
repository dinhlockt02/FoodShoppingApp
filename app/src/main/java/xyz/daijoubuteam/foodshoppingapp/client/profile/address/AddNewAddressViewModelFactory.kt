package xyz.daijoubuteam.foodshoppingapp.client.profile.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddNewAddressViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddNewAddressViewModel::class.java)){
            return AddNewAddressViewModel() as T
        }
        throw IllegalArgumentException("Unknown LoginViewModel class")
    }
}