package xyz.daijoubuteam.foodshoppingapp.client.profile.notification

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.model.Notification
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class NotificationListViewModel: ViewModel() {

    lateinit var notifications: LiveData<List<Notification>>
    lateinit var notReadNotification: LiveData<List<Notification>>

    private val userRepository = UserRepository()

    init {
        val notificationResult  = userRepository.getCurrentUserNotificationLiveData()
        if(notificationResult.isSuccess && notificationResult.getOrNull() != null) {
            notifications = notificationResult.getOrNull()!!
            notReadNotification = Transformations.map(notifications){notiList ->
                notiList.filter { noti ->
                    !noti.notificationRead
                }
            }
        }else{
            onShowMessage(notificationResult.exceptionOrNull()?.message)
        }
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private fun onShowMessage(msg: String?){
        this._message.value = msg
    }

    fun onShowMessageComplete(){
        this._message.value = "";
    }

    fun markNotificationAsRead(notification: Notification){
        viewModelScope.launch {
            val updateResult = userRepository.updateNotification(notification)
            if(updateResult.isFailure){
                onShowMessage(updateResult.exceptionOrNull()?.message)
            }
        }
    }
}