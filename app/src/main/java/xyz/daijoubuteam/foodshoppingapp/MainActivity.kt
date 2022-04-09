package xyz.daijoubuteam.foodshoppingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bundle = intent.extras
        val user = bundle?.getParcelable("USER") as FirebaseUser?
        val helloHomePage = findViewById<TextView>(R.id.hello_home_page)
        helloHomePage.text = "Hello ${user?.email.toString()}"

        val signOutButton = findViewById<Button>(R.id.signout)
        signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}