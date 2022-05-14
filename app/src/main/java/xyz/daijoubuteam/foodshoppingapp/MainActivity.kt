package xyz.daijoubuteam.foodshoppingapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
import xyz.daijoubuteam.foodshoppingapp.client.profile.address.selectlocation.SelectLocationFragment
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityMainBinding
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository
import java.util.*

class MainActivity() : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private val authRepository = AuthRepository()
    private val isUserRegisterInformation = MutableLiveData<Boolean?>(null)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //set up location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        addAuthStateListener()
        setupHomeActionBar()
        requestLocationPermission()
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

    //handle location


    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this.applicationContext,
        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        || Build.VERSION.SDK_INT < 24

    private fun requestLocationPermission() {
        if(isLocationPermissionGranted())
            return;
        else
            requestLocationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {

        } else {

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}

