package xyz.daijoubuteam.foodshoppingapp.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class LoginViewModel: ViewModel() {

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    private val authRepository = AuthRepository()

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

    private val _loginResult = MutableLiveData<Result<FirebaseUser?>?>(null)
        val loginResult: LiveData<Result<FirebaseUser?>?>
            get() = _loginResult

    var firebaseUser: FirebaseUser? = Firebase.auth.currentUser

    fun onLoginWithEmailAndPassword(){
        viewModelScope.launch {
            _loginResult.value = authRepository.loginWithEmailAndPassword(email.value!!, password.value!!)
        }
    }

    fun onLoginWithEmailAndPasswordComplete(){
        firebaseUser = _loginResult.value?.getOrNull()
        _loginResult.value = null
    }

    private val _loginWithGoogleEvent = MutableLiveData<Boolean>(false)
        val loginWithGoogleEvent:LiveData<Boolean>
                get() = _loginWithGoogleEvent

    fun onLoginWithGoogleEventTriggered(){
        _loginWithGoogleEvent.value = true
    }

    fun onLoginWithGoogleEventTriggeredComplete(){
        _loginWithGoogleEvent.value = false
    }

    fun onLoginWithGoogle(googleAuthCredential: AuthCredential){
        viewModelScope.launch {
            _loginResult.value = authRepository.loginWithGoogle(googleAuthCredential)
        }
    }

    fun onLoginWithGoogleComplete(){
        firebaseUser = _loginResult.value?.getOrNull()
        _loginResult.value = null
    }
}