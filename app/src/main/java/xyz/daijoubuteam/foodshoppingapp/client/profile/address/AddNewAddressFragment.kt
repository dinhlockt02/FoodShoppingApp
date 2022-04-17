package xyz.daijoubuteam.foodshoppingapp.client.profile.address

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.system.Os.accept
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentAddNewAddressBinding
import kotlin.concurrent.fixedRateTimer


class AddNewAddressFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentAddNewAddressBinding
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewAddressBinding.inflate(inflater, container, false)

        // Construct a PlacesClient
        Places.initialize(requireContext().applicationContext, getString(R.string.maps_api_key))
        placesClient = Places.createClient(requireContext())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapsFragment = childFragmentManager.findFragmentById(R.id.preview_map) as SupportMapFragment?


        mapsFragment?.getMapAsync(this)

        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLocationPermission(){
        if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true
            updateLocationUI()
        } else {
            requestLocationPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){permissions ->
        when{
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                locationPermissionGranted = true
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                locationPermissionGranted = true
            }

            else -> {
                locationPermissionGranted = false
            }
        }
        updateLocationUI()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Suppress("MissingPermission")
    private fun updateLocationUI() {
        if(!this::map.isInitialized){
            return
        }
        try {
            if(locationPermissionGranted){
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
                Log.i("map-current", "GOES updateLocationUI")
                getDeviceLocation()
            }else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException){
            Log.e("Exception: %s", e.message, e)
        }
    }

    @Suppress("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if(locationPermissionGranted){
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        Log.i("map-current", lastKnownLocation.toString())
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                        else {
                            Log.d("map-current", "Current location is null. Using defaults.")
                            Log.e("map-current", "Exception: ${task.exception?.message }",task.exception, )
                            map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                            map.uiSettings.isMyLocationButtonEnabled = false
                        }
                    }
                }
            }else {
                Log.d("map-current", "request permission failed.")
                map.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                map.addMarker(MarkerOptions().position(defaultLocation))
                map.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException){

        }
    }


    companion object {
        val DEFAULT_ZOOM = 17
        val defaultLocation = LatLng(10.8700,106.8031)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        updateLocationUI()
        getDeviceLocation()
    }
}