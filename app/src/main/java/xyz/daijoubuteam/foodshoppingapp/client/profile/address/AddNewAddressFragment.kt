package xyz.daijoubuteam.foodshoppingapp.client.profile.address

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.BuildConfig
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentAddNewAddressBinding


class AddNewAddressFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentAddNewAddressBinding
    private var lastKnownLocation: Location? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Navigation Args
    private val args: AddNewAddressFragmentArgs by navArgs()

    // Selected Location
    private var selectedLocation: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        selectedLocation = args.locationLatLng

        binding = FragmentAddNewAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Construct a PlacesClient
        Places.initialize(requireContext().applicationContext, BuildConfig.GOOGLE_MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val mapsFragment =
            childFragmentManager.findFragmentById(R.id.preview_map) as SupportMapFragment?
        mapsFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if (isLocationPermissionGranted()) {
            setupMap()
        } else {
            requestLocationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setupMap()
        } else {
            Snackbar.make(requireView(), R.string.request_map_explain, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            map.uiSettings.setAllGesturesEnabled(false)
            map.uiSettings.isMapToolbarEnabled = false

            if(selectedLocation !== null){
                map.addMarker(MarkerOptions().position(selectedLocation!!))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation!!, DEFAULT_ZOOM))
            } else {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(lastLocationResultListener)
            }
            map.setOnMapClickListener(mapClickListener)
        }
    }

    private val mapClickListener = GoogleMap.OnMapClickListener {
        val action = AddNewAddressFragmentDirections.actionAddNewAddressFragmentToSelectLocationFragment(selectedLocation)
        findNavController().navigate(action)
    }

    private val lastLocationResultListener = com.google.android.gms.tasks.OnCompleteListener<Location>{ task ->
        if (task.isSuccessful) {
            lastKnownLocation = task.result
            if (lastKnownLocation != null) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastKnownLocation!!.latitude,
                            lastKnownLocation!!.longitude
                        ), DEFAULT_ZOOM
                    )
                )
            } else {
                map.moveCamera(
                    CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                )
            }
        }
    }

    companion object {
        const val DEFAULT_ZOOM = 16.5f
        val defaultLocation = LatLng(10.8700, 106.8031)
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 24
}