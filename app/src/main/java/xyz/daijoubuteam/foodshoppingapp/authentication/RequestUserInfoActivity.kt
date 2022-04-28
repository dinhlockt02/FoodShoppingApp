package xyz.daijoubuteam.foodshoppingapp.authentication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityRequestUserInfoBinding
import xyz.daijoubuteam.foodshoppingapp.model.Gender
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard

class RequestUserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestUserInfoBinding
    private val authRepository = AuthRepository()
    private val auth = Firebase.auth
    private val isUserRegisterInformation = MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_user_info)
        binding.btnSubmit.setOnClickListener {
            updateUserInfo()
        }
        setupSoftKeyboardUI()
        isUserRegisterInformation.observe(this){
            if(it){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUserInfo(){
        lifecycleScope.launch {
            auth.currentUser?.let {
                val userResult = authRepository.getUserByUid(it.uid)
                if (userResult.isFailure || userResult.getOrNull() == null) {
                    auth.signOut()
                    return@launch
                }
                val user = userResult.getOrNull()!!
                user.firstname = binding.firstName.text?.trim().toString()
                user.lastname = binding.lastName.text?.trim().toString()
                user.phoneNumber = binding.phoneNumber.text?.trim().toString()
                user.gender = when(binding.genderRadioGroup.checkedRadioButtonId){
                    R.id.gender_male -> Gender.MALE
                    R.id.gender_female -> Gender.FEMALE
                    else -> Gender.OTHER
                }
                user.isUserRegisterInformation = true
                authRepository.updateUserInfo(user)
                isUserRegisterInformation.value = user.isUserRegisterInformation
            }
        }
    }

    private fun setupSoftKeyboardUI(){
        binding.firstName.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowSoftKeyboard()){
                hideKeyboard()
            }
        }
        binding.lastName.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowSoftKeyboard()){
                hideKeyboard()
            }

        }
        binding.phoneNumber.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowSoftKeyboard()){
                hideKeyboard()
            }
        }
        binding.genderRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            currentFocus?.clearFocus()
            hideKeyboard()
        }
    }

    private fun shouldShowSoftKeyboard(): Boolean{
        return binding.firstName.hasFocus() || binding.lastName.hasFocus() || binding.phoneNumber.hasFocus()
    }
}