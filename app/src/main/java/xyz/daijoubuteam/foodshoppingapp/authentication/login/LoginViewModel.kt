package xyz.daijoubuteam.foodshoppingapp.authentication.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class LoginViewModel: ViewModel() {


    private val _navigateToSignUp = MutableLiveData(false)
    private val authRepository = AuthRepository()
    private val _loginResult = MutableLiveData<Result<User?>?>(null)
    private val _navigateToForgetPassword = MutableLiveData(false)

    val loginResult: LiveData<Result<User?>?>
        get() = _loginResult
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp
    val navigateToForgetPassword: LiveData<Boolean>
        get() = _navigateToForgetPassword

    val email =  MutableLiveData("")
    val password = MutableLiveData("")
    var user: User? = null

    fun onNavigateToSignup() {
        _navigateToSignUp.value = true
    }

    fun onNavigateToSignupComplete() {
        _navigateToSignUp.value = false
    }

    fun onNavigateToForgetPassword(){
        _navigateToForgetPassword.value = true
    }

    fun onNavigateToForgetPasswordComplete(){
        _navigateToForgetPassword.value = false
    }

    fun onLoginWithEmailAndPassword(){
        if(email.value.isNullOrEmpty() || password.value.isNullOrEmpty())
        {
            val exception = Exception("Illegal email or password")
            _loginResult.value = Result.failure(exception)
            return
        }

        viewModelScope.launch {
            val credential = EmailAuthProvider.getCredential(email.value!!, password.value!!)
            _loginResult.value = authRepository.loginWithAuthCredential(credential)
        }
    }

    fun onLoginWithGoogle(googleAuthCredential: AuthCredential){
        viewModelScope.launch {
            _loginResult.value = authRepository.loginWithAuthCredential(googleAuthCredential)
        }
    }

    fun onLoginComplete(){
        user = _loginResult.value?.getOrNull()
        _loginResult.value = null
    }
}