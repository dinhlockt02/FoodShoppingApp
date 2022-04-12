package xyz.daijoubuteam.foodshoppingapp.authentication.resend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class ResendEmailResetPasswordViewModel(email: String): ViewModel() {
    private val _email = MutableLiveData(email)
    private val _navigateToLogin = MutableLiveData(false)
    private val _sendResetPasswordEmailResult = MutableLiveData<Result<Boolean>?>()
    private val authRepository = AuthRepository()

    val email: LiveData<String>
        get() = _email
    val sendResetPasswordEmailResult: LiveData<Result<Boolean>?>
        get() = _sendResetPasswordEmailResult
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun onNavigateToLogin(){
        _navigateToLogin.value = true
    }

    fun onNavigateToLoginComplete() {
        _navigateToLogin.value = false
    }

    fun onSendResetPasswordEmail(){
        viewModelScope.launch {
            _sendResetPasswordEmailResult.value = authRepository.resetPasswordWithEmail(email.value!!)
        }
    }

    fun onSendResetPasswordEmailComplete(){
        _sendResetPasswordEmailResult.value = null
    }
}