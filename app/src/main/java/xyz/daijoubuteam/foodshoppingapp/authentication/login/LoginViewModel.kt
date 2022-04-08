package xyz.daijoubuteam.foodshoppingapp.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class LoginViewModel: ViewModel() {

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    private val authRepository = AuthRepository()
    var user: User? = null
        get() = field

    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    fun onNavigateToSignup() {
        _navigateToSignUp.value = true
    }

    fun onNavigateToSignupComplete() {
        _navigateToSignUp.value = false
    }

    val email =  MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    private val _loginResult = MutableLiveData<Result<User?>?>(null)
        val loginResult: LiveData<Result<User?>?>
            get() = _loginResult

    fun onLoginWithEmailAndPassword(){
        viewModelScope.launch {
            _loginResult.value = authRepository.loginWithEmailAndPassword(email.value!!, password.value!!)
        }
    }

    fun onLoginWithEmailAndPasswordComplete(){
        user = _loginResult.value?.getOrNull()
        _loginResult.value = null
    }
}