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
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class SignUpViewModel: ViewModel() {
    private val _navigateToLogin = MutableLiveData(false)
    private val authRepository = AuthRepository()

    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun onNavigateToLogin() {
        _navigateToLogin.value = true
    }

    fun onNavigateToLoginComplete() {
        _navigateToLogin.value = false
    }

    val email =  MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    private var _signUpResult =  MutableLiveData<Result<FirebaseUser?>?>()
        val signUpResult: LiveData<Result<FirebaseUser?>?>
            get() = _signUpResult

    fun onSignUpWithEmailAndPassword(){
        if(email.value.isNullOrEmpty() || password.value.isNullOrEmpty())
        {
            val exception = Exception("Illegal email or password")
            _signUpResult.value = Result.failure(exception)
            return
        }
        if(confirmPassword.value.isNullOrEmpty() || !password.value.equals(confirmPassword.value)){
            val exception = Exception("Confirm password not match")
            _signUpResult.value = Result.failure(exception)
            return
        }
        viewModelScope.launch {
            _signUpResult.value = authRepository.signupWithEmailAndPassword(email.value!!, password.value!!)
        }
        onSignUpWithEmailAndPasswordComplete()
    }

    fun onSignUpWithGoogle(googleAuthCredential: AuthCredential){
        viewModelScope.launch {
            _signUpResult.value = authRepository.loginWithAuthCredential(googleAuthCredential)
        }
        onSignUpWithGoogleComplete()
    }

    fun onSignUpWithGoogleComplete(){
        _signUpResult.value = null
    }

    fun onSignUpWithEmailAndPasswordComplete(){
        Firebase.auth.signOut()
       _signUpResult.value = null
    }
}