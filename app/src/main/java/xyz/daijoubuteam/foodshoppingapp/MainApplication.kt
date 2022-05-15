package xyz.daijoubuteam.foodshoppingapp

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

class MainApplication: Application() {
    val location: MutableLiveData<Location> = MutableLiveData<Location>()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}