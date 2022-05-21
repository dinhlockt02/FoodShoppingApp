package xyz.daijoubuteam.foodshoppingapp.authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        addAuthStateListener()
        setupActionBar()
    }

    private fun setupActionBar(){
        binding.authToolbar.setTitleTextColor(Color.TRANSPARENT)
        setSupportActionBar(binding.authToolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.login_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(binding.authToolbar, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { controller: NavController?, destination: NavDestination, arguments: Bundle? ->
            binding.authToolbar.setNavigationIcon(R.drawable.img_chevronleft)
        }
    }


    private fun addAuthStateListener() {
        auth.addAuthStateListener { it ->
            it.currentUser?.let { user ->
                if(user.isEmailVerified){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val intent = Intent(this, VerifyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

        }
    }


}
