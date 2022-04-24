package xyz.daijoubuteam.foodshoppingapp.client.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class ProfileViewModel: ViewModel() {
    private val userRepository = UserRepository()
    lateinit var user: LiveData<User>
    private var _notification = MutableLiveData(0)
        val notification
            get() = _notification
    init {
        val userResult = userRepository.getCurrentUserLiveData()
        if(userResult.isSuccess && userResult.getOrNull() !== null){
            user = userResult.getOrNull()!!
        }else {
            onShowError(userResult.exceptionOrNull()?.message)
        }
    }

    private val _errMessage = MutableLiveData("")
    val errMessage: LiveData<String>
        get() = _errMessage

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }

    fun onSignout(){
        Firebase.auth.signOut()
    }
}