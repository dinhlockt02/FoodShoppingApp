package xyz.daijoubuteam.foodshoppingapp.client.saved

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class SavedViewModel : ViewModel() {
    private val userRepository = UserRepository()


    lateinit var favoriteEateries: LiveData<List<Eatery?>>


    init {
        val result = userRepository.getFavoriteEateries()
        if (result.isFailure) {
        } else if (result.getOrNull() != null) {
            favoriteEateries = result.getOrNull()!!
        }
    }

    val emptyImageVisibility: LiveData<Int> = Transformations.map(favoriteEateries){
        if(it.isNullOrEmpty()){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    fun unfavorite(eatery: Eatery) {
        viewModelScope.launch {
            userRepository.removeFromFavorite(eatery)
        }
    }
}
