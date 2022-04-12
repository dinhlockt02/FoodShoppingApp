package xyz.daijoubuteam.foodshoppingapp.authentication.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgetPasswordViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)){
            return ForgetPasswordViewModel() as T
        }
        throw IllegalArgumentException("Unknown SignupViewModel class")
    }
}