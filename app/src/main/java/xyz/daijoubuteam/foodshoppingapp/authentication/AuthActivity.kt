package xyz.daijoubuteam.foodshoppingapp.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R

class AuthActivity : AppCompatActivity() {

    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        addAuthStateListener()
    }

    private fun addAuthStateListener() {
        auth.addAuthStateListener { it ->
            it.currentUser?.let { user ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }


}
