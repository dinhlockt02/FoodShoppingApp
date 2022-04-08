package xyz.daijoubuteam.foodshoppingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import xyz.daijoubuteam.foodshoppingapp.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bundle = intent.extras
        val user = bundle?.getParcelable("USER") as User?
        val helloHomePage = findViewById<TextView>(R.id.hello_home_page)
        helloHomePage.text = "Hello ${user?.email.toString()}"
    }
}