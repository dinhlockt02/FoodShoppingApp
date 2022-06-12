package xyz.daijoubuteam.foodshoppingapp.client.home.search

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.repositories.SearchRepository
import java.lang.Exception

class SearchViewModel: ViewModel() {
    val searchText = MutableLiveData("")
    private var job: Job? = null
    private val _eatery = MutableLiveData<List<Eatery>>()
    private val searchRepository = SearchRepository()

    val eatery: LiveData<List<Eatery>>
        get() = _eatery

    val emptyCartImageVisibility: LiveData<Int> = Transformations.map(_eatery){
        if(it.isNullOrEmpty()){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    val eateryListVisibility: LiveData<Int> = Transformations.map(_eatery){
        if(it.isNullOrEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }
    }

    fun search() {
        job?.cancel("Search text changed")
        job = viewModelScope.launch {
            delay(500)
            _eatery.value = searchRepository.searchEateries(searchText.value?.trim())
        }
    }

    suspend fun findFullEateryInfo(id: String?): Result<Eatery> {
        if(id.isNullOrEmpty()) return Result.failure(Exception("Id is null"))
        val eatery = searchRepository.getEateryById(id)
            ?: return Result.failure(Exception("Fetch eatery failed"))
        return Result.success(eatery)
    }
}