package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

class DetailEateryViewModelFactory(
    private val eateryProperty: Eatery,
    private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailEateryViewModel::class.java)){
            return DetailEateryViewModel(eateryProperty,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}