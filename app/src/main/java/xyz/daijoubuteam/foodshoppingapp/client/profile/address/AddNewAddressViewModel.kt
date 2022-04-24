package xyz.daijoubuteam.foodshoppingapp.client.profile.address

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class AddNewAddressViewModel : ViewModel() {
    val shippingAddress = MutableLiveData(ShippingAddress())
    private val userRepository = UserRepository()
    private var user: User? = null
    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message
    private val _saveAddressEventComplete = MutableLiveData(false)
    val saveAddressEventComplete: LiveData<Boolean>
        get() = _saveAddressEventComplete

    fun setAddress(address: String) {
        shippingAddress.value = shippingAddress.value?.also {
            it.address = address
        }
    }

    private fun isAddressInputValid(): Boolean {
        return !shippingAddress.value?.name.isNullOrBlank()
                && !shippingAddress.value?.phoneNumber.isNullOrBlank()
                && !shippingAddress.value?.address.isNullOrBlank()
    }

    fun onSaveAddress(){
        if(isAddressInputValid())
        {
            viewModelScope.launch {
                val userResult = userRepository.getCurrentUser()
                if(userResult.isFailure){
                    _message.value = userResult.exceptionOrNull()?.message
                    return@launch
                }
                user = userResult.getOrNull()
                if(user == null){
                    _message.value = "Can not get current user"
                    return@launch
                }
                shippingAddress.value?.let {currentShippingAddress ->
                    val existShippingAddressIndex = user!!.shippingAddresses.indexOfFirst {
                        it.id == currentShippingAddress.id
                    }
                    if(existShippingAddressIndex == -1)
                        user!!.shippingAddresses.add(currentShippingAddress)
                    else
                        user!!.shippingAddresses[existShippingAddressIndex] = currentShippingAddress
                }
                val updateResult = userRepository.updateCurrentUserInfo(user!!)
                if(updateResult.isSuccess){
                    _saveAddressEventComplete.value = updateResult.getOrNull()
                }else {
                    _message.value = updateResult.exceptionOrNull()?.message.toString()
                    _saveAddressEventComplete.value = false
                }
            }
        } else {
            _message.value = "Invalid address input"
        }
    }
    fun onSaveAddressComplete(){
        _saveAddressEventComplete.value = false
    }
}