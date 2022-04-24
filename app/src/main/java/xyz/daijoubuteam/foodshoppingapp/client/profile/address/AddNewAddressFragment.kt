package xyz.daijoubuteam.foodshoppingapp.client.profile.address

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.BuildConfig
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentAddNewAddressBinding
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress
import java.util.*


class AddNewAddressFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentAddNewAddressBinding

    private val viewmodel: AddNewAddressViewModel by lazy{
        val factory = AddNewAddressViewModelFactory()
        ViewModelProvider(this, factory)[AddNewAddressViewModel::class.java]
    }

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
        binding.viewmodel = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelObserver()
        val mapsFragment =
            childFragmentManager.findFragmentById(R.id.preview_map) as SupportMapFragment?
        mapsFragment?.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if (isLocationPermissionGranted()) {
            setupMap()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setupViewModelObserver(){
        binding.lifecycleOwner = viewLifecycleOwner
        viewmodel.saveAddressEventComplete.observe(viewLifecycleOwner){
            if(it){
                val action = AddNewAddressFragmentDirections.actionAddNewAddressFragmentToProfileAddressEditFragment()
                findNavController().navigate(action)
                viewmodel.onSaveAddressComplete()
            }
        }
        viewmodel.message.observe(viewLifecycleOwner){
            if(!it.isNullOrBlank()){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            map.uiSettings.setAllGesturesEnabled(false)
            map.uiSettings.isMapToolbarEnabled = false

            if(selectedLocation !== null){
                map.addMarker(MarkerOptions().position(selectedLocation!!).title(getString(R.string.your_shipping_location_is_here)))
                findAddress(selectedLocation!!)
                viewmodel.shippingAddress.value?.location = selectedLocation!!
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation!!, DEFAULT_ZOOM))
                map.setOnMapClickListener(mapClickListener)
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private val mapClickListener = GoogleMap.OnMapClickListener {
        val action = AddNewAddressFragmentDirections.actionAddNewAddressFragmentToSelectLocationFragment(selectedLocation)
        findNavController().navigate(action)
    }

    companion object {
        const val DEFAULT_ZOOM = 16.5f
    }

    private fun findAddress(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses[0]?.let { address ->
            if(address.maxAddressLineIndex != -1)
            {
                viewmodel.setAddress(address.getAddressLine(0))
            }
        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 24
}