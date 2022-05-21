package xyz.daijoubuteam.foodshoppingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.VerifyActivity

class SpashActivity : AppCompatActivity() {

    private val routingComplete = MutableLiveData(false)
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        val splashScreen = installSplashScreen()
        val keepOnScreenCondition = SplashScreen.KeepOnScreenCondition {
            return@KeepOnScreenCondition (routingComplete.value != true)
        }
        splashScreen.setKeepOnScreenCondition(keepOnScreenCondition)
        setContentView(R.layout.activity_spash)
        startRouting()
    }


    private fun startRouting() {
        when {
            auth.currentUser == null -> {
                val intent = Intent(this@SpashActivity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            auth.currentUser?.isEmailVerified != true -> {
                val intent = Intent(this@SpashActivity, VerifyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else -> {
                val intent = Intent(this@SpashActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        routingComplete.value = true
    }
}