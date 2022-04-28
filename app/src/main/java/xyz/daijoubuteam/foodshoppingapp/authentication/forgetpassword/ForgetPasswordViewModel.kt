package xyz.daijoubuteam.foodshoppingapp.authentication.forgetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class ForgetPasswordViewModel : ViewModel() {
    private val _navigateToLogin = MutableLiveData(false)
    private val _sendResetPasswordEmailResult = MutableLiveData<Result<Boolean>?>()
    private val authRepository = AuthRepository()
    private val _message = MutableLiveData("")

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin
    val sendResetPasswordEmailResult: LiveData<Result<Boolean>?>
        get() = _sendResetPasswordEmailResult
    val email = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    fun onNavigateToLogin() {
        _navigateToLogin.value = true
    }

    fun onNavigateToLoginComplete() {
        _navigateToLogin.value = false
    }

    fun onSendResetPasswordEmail() {
        viewModelScope.launch {
            _sendResetPasswordEmailResult.value =
                authRepository.resetPasswordWithEmail(email.value!!)
        }
    }

    fun onSendResetPasswordEmailComplete() {
        _sendResetPasswordEmailResult.value = null
    }

    fun onShowMessage(msg: String) {
        _message.value = msg
    }

    fun onShowMessageComplete() {
        _message.value = ""
    }
}