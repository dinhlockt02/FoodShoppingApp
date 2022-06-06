package xyz.daijoubuteam.foodshoppingapp.client.home.carousel_detail

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.model.Event

class CarouselDetailViewModelFactory(
    private val event: Event,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CarouselDetailViewModel::class.java)) {
            return CarouselDetailViewModel(event, application) as T
        }
        throw IllegalArgumentException("Unknown SignupViewModel class")
    }
}