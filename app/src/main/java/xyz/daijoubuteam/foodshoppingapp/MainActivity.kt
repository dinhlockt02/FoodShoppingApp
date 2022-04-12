package xyz.daijoubuteam.foodshoppingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.VerifyActivity
import xyz.daijoubuteam.foodshoppingapp.client.feeds.FeedsFragment
import xyz.daijoubuteam.foodshoppingapp.client.home.HomeFragment
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrdersFragment
import xyz.daijoubuteam.foodshoppingapp.client.saved.SavedFragment
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        addAuthStateListener()
    }

    override fun onStart() {
        super.onStart()
        val navController = Navigation.findNavController(this, R.id.navHomeFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }
}