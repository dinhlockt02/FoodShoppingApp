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

class ProfileViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val authRepository = AuthRepository()
    private var _user = MutableLiveData<User>(null)
        val user: LiveData<User>
            get() = _user
    private var _notification = MutableLiveData(0)
        val notification
            get() = _notification
    init {
        viewModelScope.launch {
            auth.uid?.let { uid ->
                val userResult = authRepository.getUserByUid(uid)
                if(userResult.isSuccess){
                    _user.value = userResult.getOrNull()
                }else
                {
                    onShowError(userResult.exceptionOrNull()?.localizedMessage)
                }
            }
        }
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private fun onShowError(msg: String?){
        this._message.value = msg
    }
}