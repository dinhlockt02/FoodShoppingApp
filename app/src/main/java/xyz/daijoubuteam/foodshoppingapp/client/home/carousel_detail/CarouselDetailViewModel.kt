package xyz.daijoubuteam.foodshoppingapp.client.home.carousel_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Event
import xyz.daijoubuteam.foodshoppingapp.repositories.EventRepository


class CarouselDetailViewModel(event: Event, app: Application): AndroidViewModel(app) {
    private val eventRepository = EventRepository()
    private val eventSelected = event

    private lateinit var _eateryList: LiveData<List<Eatery>>
    private val _errMessage = MutableLiveData("")
    val eateryList: LiveData<List<Eatery>>
        get() = _eateryList
    val errMessage: LiveData<String>
        get() = _errMessage

    private fun getEateryListFollowEvent() {
        val eateryListResult = eventSelected.id?.let {
            eventRepository.fetchEateryFollowEvent(it)
        }
        if (eateryListResult != null) {
            if(eateryListResult.isSuccess && eateryListResult.getOrNull() !== null){
                _eateryList = eateryListResult.getOrNull()!!
            }
            else {
                onShowError(eateryListResult.exceptionOrNull()?.message)
            }
        }
    }
    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }



}