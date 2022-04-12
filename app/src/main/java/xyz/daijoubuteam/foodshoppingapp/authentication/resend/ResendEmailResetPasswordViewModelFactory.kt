package xyz.daijoubuteam.foodshoppingapp.authentication.resend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResendEmailResetPasswordViewModelFactory(val email: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ResendEmailResetPasswordViewModel::class.java)){
            return ResendEmailResetPasswordViewModel(email) as T
        }
        throw IllegalArgumentException("Unknown SignupViewModel class")
    }
}