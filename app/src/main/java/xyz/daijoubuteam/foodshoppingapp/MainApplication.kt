package xyz.daijoubuteam.foodshoppingapp

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber


class MainApplication : Application() {
    val location: MutableLiveData<Location> = MutableLiveData<Location>()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.i(it.result);
            }
        }
        createNotificationChannel()
        registerForNetworkUpdate()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(getString(R.string.CHANNEL_ID), name, importance).apply {
                    description = descriptionText
                }
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            channel.setSound(alarmSound, att)
            channel.enableVibration(true)
            channel.enableLights(true);

            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC;
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val _hasConnection = MutableLiveData(false)
    val hasConnection: LiveData<Boolean>
        get() = _hasConnection


    @SuppressLint("MissingPermission")
    private fun registerForNetworkUpdate() {
        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val currentNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        if(caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            _hasConnection.value = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network : Network) {
                    _hasConnection.postValue(true)
                }

                override fun onLost(network : Network) {
                    _hasConnection.postValue(false)
                }

            })
        }
    }
}