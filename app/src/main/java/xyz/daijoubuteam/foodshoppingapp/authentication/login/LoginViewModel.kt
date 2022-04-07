package xyz.daijoubuteam.foodshoppingapp.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    fun onNavigateToSignup() {
        _navigateToSignUp.value = true
    }

    fun onNavigateToSignupComplete() {
        _navigateToSignUp.value = false
    }
}