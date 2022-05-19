package xyz.daijoubuteam.foodshoppingapp.client.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.model.Notification
import xyz.daijoubuteam.foodshoppingapp.model.User
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class ProfileViewModel: ViewModel() {
    private val userRepository = UserRepository()
    lateinit var user: LiveData<User>
    lateinit var notifications: LiveData<List<Notification>>
    lateinit var notReadNotification: LiveData<List<Notification>>


    init {
        val userResult = userRepository.getCurrentUserLiveData()
        if(userResult.isSuccess && userResult.getOrNull() !== null){
            user = userResult.getOrNull()!!
        }else {
            onShowError(userResult.exceptionOrNull()?.message)
        }
        val notificationResult  = userRepository.getCurrentUserNotificationLiveData()
        if(notificationResult.isSuccess && notificationResult.getOrNull() != null) {
            notifications = notificationResult.getOrNull()!!
            notReadNotification = Transformations.map(notifications){ notiList ->
                notiList.filter { noti ->
                    !noti.notificationRead
                }
            }
        }else{
            onShowError(notificationResult.exceptionOrNull()?.message)
        }
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private fun onShowError(msg: String?){
        this._message.value = msg
    }

    fun onSignout(){
        Firebase.auth.signOut()
    }
}