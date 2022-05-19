package xyz.daijoubuteam.foodshoppingapp.client.home.vertical_list_eateries

import android.app.Application
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import xyz.daijoubuteam.foodshoppingapp.client.home.TypesViewAll
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.repositories.EateryRepository

class VerticalListEateryViewModel(typeListEatery: TypesViewAll, app:Application): AndroidViewModel(app) {
    private val eateryRepository = EateryRepository()
    private lateinit var _eateryList: LiveData<List<Eatery>>
    private lateinit var _popularEateryList: LiveData<List<Eatery>>
    private lateinit var _nearlyEateryList: LiveData<List<Eatery>>
    private val _errMessage = MutableLiveData("")
    private val _navigateToSelectedEatery = MutableLiveData<Eatery>()
    private val _typeList: TypesViewAll = typeListEatery
    val errMessage: LiveData<String>
        get() = _errMessage
    val navigateToSelectedEatery: LiveData<Eatery>
        get() = _navigateToSelectedEatery
    val typeList: TypesViewAll
        get() = _typeList
    val eateryList: LiveData<List<Eatery>>
        get() = _eateryList
    val nearlyEateryList: LiveData<List<Eatery>>
        get() = _nearlyEateryList
    val popularEateryList: LiveData<List<Eatery>>
        get() = _popularEateryList
    init {
        getEateryList()
        if(_typeList == TypesViewAll.POPULAR) {
            getPopularEateryList()
        }
        else {
            getNearEateryList()
        }

    }
    private fun getEateryList() {
        val eateryListResult = eateryRepository.getListEatery()
        if(eateryListResult.isSuccess && eateryListResult.getOrNull() !== null){
            _eateryList = eateryListResult.getOrNull()!!
        }else {
            onShowError(eateryListResult.exceptionOrNull()?.message)
        }
    }
    private fun getPopularEateryList() {
        getEateryList()
        _popularEateryList = Transformations.map(eateryList, Function { _eateryList ->
            _eateryList.sortedByDescending { it.average_rating_count }
        })
    }
    private fun getNearEateryList() {
        getEateryList()
        _nearlyEateryList = _eateryList
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
    fun onNavigateToSelectedEateryComplete(){
        _navigateToSelectedEatery.value = null
    }
    //show detail eatery selected
    fun displayPropertyDetailEatery(eaterySelected: Eatery) {
        _navigateToSelectedEatery.value = eaterySelected
    }
}