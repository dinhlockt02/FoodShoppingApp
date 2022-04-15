package xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class ProfileAddressEditViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val authRepository = AuthRepository()
    var user = MutableLiveData<User>(null)

    init {
        viewModelScope.launch {
            auth.uid?.let { uid ->
                val userResult = authRepository.getUserByUid(uid)
                if(userResult.isSuccess){
                    user.value = userResult.getOrNull()
                }else
                {
                    onShowError(userResult.exceptionOrNull()?.localizedMessage)
                }
            }
        }
    }

    private val _errMessage = MutableLiveData("")
    val errMessage: LiveData<String>
        get() = _errMessage

    private fun onShowError(msg: String?){
        this._errMessage.value = msg
    }
}