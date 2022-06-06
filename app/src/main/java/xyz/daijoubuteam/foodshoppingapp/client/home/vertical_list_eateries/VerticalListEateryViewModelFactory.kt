package xyz.daijoubuteam.foodshoppingapp.client.home.vertical_list_eateries

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.MainApplication
import xyz.daijoubuteam.foodshoppingapp.client.home.HomeViewModel
import xyz.daijoubuteam.foodshoppingapp.client.home.TypesViewAll

class VerticalListEateryViewModelFactory (
    private val typesEatery: TypesViewAll,
    private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VerticalListEateryViewModel::class.java)) {
            return VerticalListEateryViewModel(typesEatery, application) as T
        }
        throw IllegalArgumentException("Unknown SignupViewModel class")
    }
}