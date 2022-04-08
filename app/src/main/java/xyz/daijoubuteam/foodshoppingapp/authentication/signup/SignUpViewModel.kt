package xyz.daijoubuteam.foodshoppingapp.authentication.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class SignUpViewModel: ViewModel() {
    private val _navigateToLogin = MutableLiveData<Boolean>(false)
    private val authRepository = AuthRepository()

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun onNavigateToLogin() {
        _navigateToLogin.value = true
    }

    fun onNavigateToLoginComplete() {
        _navigateToLogin.value = false
    }

    val email =  MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val confirmPassword = MutableLiveData<String>("")




    private var _signUpResult =  MutableLiveData<Result<User>?>()
        val signUpResult: LiveData<Result<User>?>
            get() = _signUpResult
    fun onSignUpWithEmailAndPassword(){
        viewModelScope.launch {
            _signUpResult.value = authRepository.signUpWithEmailAndPassword(email.value!!, password.value!!)
        }
    }

    fun onSignUpWithEmailAndPasswordComplete(){
       _signUpResult.value = null
    }
}