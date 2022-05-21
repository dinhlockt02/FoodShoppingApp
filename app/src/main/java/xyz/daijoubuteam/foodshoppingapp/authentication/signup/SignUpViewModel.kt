package xyz.daijoubuteam.foodshoppingapp.authentication.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class SignUpViewModel: ViewModel() {
    private val _navigateToLogin = MutableLiveData(false)
    private val authRepository = AuthRepository()
    private val _message = MutableLiveData("")

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    val message: LiveData<String>
        get() = _message

    fun onNavigateToLogin() {
        _navigateToLogin.value = true
    }

    fun onNavigateToLoginComplete() {
        _navigateToLogin.value = false
    }

    val email =  MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    private var _signUpResult =  MutableLiveData<Result<User?>?>()
        val signUpResult: LiveData<Result<User?>?>
            get() = _signUpResult

    fun onSignUpWithEmailAndPassword(){
        if(email.value.isNullOrEmpty() || password.value.isNullOrEmpty())
        {
            val exception = Exception("Illegal email or password")
            _signUpResult.value = Result.failure(exception)
            return
        }
        if(confirmPassword.value.isNullOrEmpty() || !password.value!!.trim().equals(confirmPassword.value!!.trim())){
            val exception = Exception("Confirm password not match")
            _signUpResult.value = Result.failure(exception)
            return
        }
        viewModelScope.launch {
            _signUpResult.value = authRepository.signupWithEmailAndPassword(email.value!!.trim(), password.value!!.trim())
        }
    }

    fun onSignUpWithGoogle(googleAuthCredential: AuthCredential){
        viewModelScope.launch {
            _signUpResult.value = authRepository.loginWithAuthCredential(googleAuthCredential)
        }
    }


    fun onSignUpComplete(){
        _signUpResult.value = null
    }


    fun onShowMessage(msg: String){
        _message.value = msg
    }

    fun onShowMessageComplete(){
        _message.value = ""
    }
}