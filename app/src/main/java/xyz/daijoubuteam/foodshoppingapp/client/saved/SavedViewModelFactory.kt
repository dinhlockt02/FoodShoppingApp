package xyz.daijoubuteam.foodshoppingapp.client.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SavedViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SavedViewModel::class.java)){
            return SavedViewModel() as T
        }
        throw IllegalArgumentException("Unknown SignupViewModel class")
    }

}