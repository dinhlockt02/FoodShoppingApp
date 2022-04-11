package xyz.daijoubuteam.foodshoppingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.VerifyActivity

class MainActivity : AppCompatActivity() {

    private val auth = Firebase.auth

    private fun addAuthStateListener() {
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (!it.currentUser!!.isEmailVerified) {
                val intent = Intent(this, VerifyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val helloHomePage = findViewById<TextView>(R.id.hello_home_page)
        helloHomePage.text = "Hello ${auth.currentUser?.email.toString()}"

        val signOutButton = findViewById<Button>(R.id.signout)
        signOutButton.setOnClickListener {
            Firebase.auth.signOut()
        }
        addAuthStateListener()
    }

}