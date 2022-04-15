package xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileAddressEditViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileAddressEditViewModel::class.java)){
            return ProfileAddressEditViewModel() as T
        }
        throw IllegalArgumentException("Unknown LoginViewModel class")
    }

}