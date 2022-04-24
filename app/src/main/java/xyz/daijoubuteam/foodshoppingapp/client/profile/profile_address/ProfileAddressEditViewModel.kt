package xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class ProfileAddressEditViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val userRepository = UserRepository()
    var user = MutableLiveData<User>(null)

    init {
        viewModelScope.launch {
            val userResult = userRepository.getCurrentUser()
            if (userResult.isSuccess) {
                user.value = userResult.getOrNull()
            } else {
                onShowMessage(userResult.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private fun onShowMessage(msg: String?) {
        this._message.value = msg
    }

    fun onShowMessageComplete(){
        this._message.value = ""
    }

    fun onSaveUserData() {
        viewModelScope.launch {
            try {
                if(user.value == null){
                    throw Exception("User not found")
                }
                val updateResult = userRepository.updateCurrentUserInfo(user.value!!)
                if(updateResult.isFailure){
                    val exception = updateResult.exceptionOrNull()
                    if(exception != null){
                        throw exception
                    }else
                    {
                        throw Exception("Save failed")
                    }
                }else if(updateResult.isSuccess){
                    onShowMessage("Save successful")
                }
            }catch (exception: Exception){
                onShowMessage(exception.message)
            }
        }
    }
}