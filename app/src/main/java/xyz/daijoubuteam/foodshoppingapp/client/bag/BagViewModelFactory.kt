package xyz.daijoubuteam.foodshoppingapp.client.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.client.profile.address.AddNewAddressViewModel

class BagViewModelFactory: ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BagViewModel::class.java)){
            return BagViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}