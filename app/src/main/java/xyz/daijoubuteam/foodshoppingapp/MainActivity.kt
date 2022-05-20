package xyz.daijoubuteam.foodshoppingapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.RequestUserInfoActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.VerifyActivity
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityMainBinding
import xyz.daijoubuteam.foodshoppingapp.repositories.AuthRepository
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.client.home.HomeFragment
import java.util.*
import kotlin.collections.ArrayList

class MainActivity() : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private val authRepository = AuthRepository()
    private val isUserRegisterInformation = MutableLiveData<Boolean?>(null)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _userLocation: MutableLiveData<Location> = MutableLiveData<Location>()
    val userLocation
            get () = _userLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        GoogleApiAvailability().makeGooglePlayServicesAvailable(this)
        //set up location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if(auth.currentUser != null) {
            getLastLocation()
        }
        addAuthStateListener()
        setupHomeActionBar()
        addConnectionStateListener()
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

    private fun addConnectionStateListener(){
        (application as? MainApplication)?.hasConnection?.observe(this){
            if(it == true) {
                binding.blockInteractionLayout.visibility = View.GONE
            }else {
                binding.blockInteractionLayout.visibility = View.VISIBLE
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

    //Get current user location
    private fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    private fun requestPermission(){
        requestLocationPermission.launch(listOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).toTypedArray())
    }

    private fun isLocationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationClient.lastLocation.addOnCompleteListener {task->
                    val location:Location? = task.result
                    if(location == null){
                        newLocationData()
                    }else{
                        _userLocation.value = location!!
                        val mainApplication = application as? MainApplication
                        mainApplication?.location?.value = location
                        Timber.i(location.toString())
                        //textView.text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n" + getCityName(location.latitude,location.longitude)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocationData(){
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            priority = PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Looper.myLooper()?.let {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,locationCallback, it
            )
        }
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            _userLocation.value = lastLocation
            val mainApplication = application as? MainApplication

            if(mainApplication?.location?.value != null) {
                lastLocation = mainApplication.location.value!!
            }
            mainApplication?.location?.observe(this@MainActivity){
                Timber.i(it.toString())
                if(it != null) {
                    lastLocation = it
                }
            }
        }
    }

    private val requestLocationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if(it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            getLastLocation()
        }
        else if(it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getLastLocation()
        }
        else
        {
            //thong bao
            Firebase.auth.signOut()
        }
    }


}

