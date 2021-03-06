package xyz.daijoubuteam.foodshoppingapp.client.home.categories_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.repositories.CategoryRepository

class CategoryDetailViewModel(category: Category,app: Application ): AndroidViewModel(app) {
    private val categoryRepository = CategoryRepository()
    private val _categorySelected = category
    private lateinit var _eateryList: LiveData<List<Eatery>>
    private val _errMessage = MutableLiveData("")
    private val _navigateToSelectedEatery = MutableLiveData<Eatery>()
    val errMessage: LiveData<String>
        get() = _errMessage
    val eateryList: LiveData<List<Eatery>>
        get() = _eateryList
    val navigateToSelectedEatery: LiveData<Eatery>
        get() = _navigateToSelectedEatery
    init {
        getEateryListByCategory()
    }

    private fun getEateryListByCategory() {
        val eateryListResult = _categorySelected.name?.let {
            categoryRepository.getListEateryByCategory(it)
        }

        if (eateryListResult != null) {
            if(eateryListResult.isSuccess && eateryListResult.getOrNull() !== null) {
                _eateryList = eateryListResult.getOrNull()!!
            } else {
                onShowError(eateryListResult.exceptionOrNull()?.message)
            }
        }
        Timber.i(_eateryList.value.toString())
    }

    //show detail eatery selected
    fun displayPropertyDetailEatery(eaterySelected: Eatery) {
        _navigateToSelectedEatery.value = eaterySelected
    }

    fun onNavigateToSelectedEateryComplete(){
        _navigateToSelectedEatery.value = null
    }

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
}