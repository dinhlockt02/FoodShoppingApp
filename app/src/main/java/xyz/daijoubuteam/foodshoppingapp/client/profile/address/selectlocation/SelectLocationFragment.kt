package xyz.daijoubuteam.foodshoppingapp.client.profile.address.selectlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.snackbar.Snackbar
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.profile.address.AddNewAddressFragment
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentSelectLocationBinding

class SelectLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var map: GoogleMap

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Marker Object
    private var marker: Marker? = null

    // Navigation Args
    private val args: SelectLocationFragmentArgs by navArgs()

    // Selected Location
    private var selectedLocation: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        selectedLocation = args.locationLatLng

        // Inflate the layout for this fragment
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapsFragment = childFragmentManager.findFragmentById(R.id.select_map) as SupportMapFragment?

        mapsFragment?.getMapAsync(this)
        binding.selectBtn.setOnClickListener(selectButtonClickListener)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if(isLocationPermissionGranted()){
            setupMap()
        }else {
            requestLocationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setupMap()
        } else {
            findNavController().navigateUp()
        }
    }



    @SuppressLint("MissingPermission")
    private fun setupMap() {
        map.isMyLocationEnabled = true
        if(selectedLocation !== null){
            marker = map.addMarker(MarkerOptions().position(selectedLocation!!))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation!!, DEFAULT_ZOOM))
        }
        else {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(lastLocationResultListener)
        }
        map.setOnMapClickListener(mapOnClickListener)
    }

    private val lastLocationResultListener = com.google.android.gms.tasks.OnCompleteListener<Location>{ task ->
        if (task.isSuccessful) {
            val lastKnownLocation = task.result
            if (lastKnownLocation != null) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastKnownLocation.latitude,
                            lastKnownLocation.longitude
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

    private val mapOnClickListener = GoogleMap.OnMapClickListener {
        if(marker == null){
            marker = map.addMarker(MarkerOptions().position(it).title(getString(R.string.your_shipping_location_is_here)))
        }else {
            marker!!.position = it
        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 24


    private val selectButtonClickListener = View.OnClickListener {
        if(marker == null){
            Snackbar.make(requireView(), "Please select location", Snackbar.LENGTH_LONG).show()
        }else {
            val action = SelectLocationFragmentDirections.actionSelectLocationFragmentToAddNewAddressFragment(marker!!.position)
            findNavController().navigate(action)
        }
    }


    companion object {
        const val DEFAULT_ZOOM = 16.5f
        val defaultLocation = LatLng(10.8700, 106.8031)
    }
}