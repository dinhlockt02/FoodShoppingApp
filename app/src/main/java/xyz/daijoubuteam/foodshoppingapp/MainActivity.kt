package xyz.daijoubuteam.foodshoppingapp

import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.RequestUserInfoActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.VerifyActivity
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityMainBinding
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository

class MainActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val authRepository = AuthRepository()
    private val isUserRegisterInformation = MutableLiveData<Boolean?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        addAuthStateListener()
        setupHomeActionBar()
    }

    override fun onStart() {
        super.onStart()
        val navController = Navigation.findNavController(this, R.id.navHomeFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (binding.bottomNavigation.menu.findItem(destination.id) == null) {
                binding.bottomNavigation.visibility = View.GONE
            }else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }

        }
        lifecycleScope.launch {
            auth.currentUser?.let {
                val userResult = authRepository.getUserByUid(it.uid)
                if (userResult.isFailure || userResult.getOrNull() == null) {
                    auth.signOut()
                    return@launch
                }
                val user = userResult.getOrNull()!!
                isUserRegisterInformation.value = user.isUserRegisterInformation
            }
        }

    }

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

        isUserRegisterInformation.observe(this) {
            if (it == false) {
                val intent = Intent(this@MainActivity, RequestUserInfoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun setMenuSelectedItem(id: Int) {
        if (id == binding.bottomNavigation.selectedItemId || !this::binding.isInitialized)
            return
        binding.bottomNavigation.menu.findItem(id)?.let {
            binding.bottomNavigation.selectedItemId = it.itemId
        }
    }

    private fun setupHomeActionBar(){
        setSupportActionBar(binding.homeToolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHomeFragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(binding.homeToolbar, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { controller: NavController?, destination: NavDestination, arguments: Bundle? ->
            binding.homeToolbar.setNavigationIcon(R.drawable.img_chevronleft)
        }
    }

}