package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.profile.address.AddNewAddressFragment
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentDetailEateryInforBinding

class DetailEateryInforFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentDetailEateryInforBinding
    private val viewModel: DetailEateryViewModel by lazy {
        val application = requireNotNull(activity).application
        val eateryProperty = DetailEateryInforFragmentArgs.fromBundle(requireArguments()).eatery
        val viewModelFactory = DetailEateryViewModelFactory(eateryProperty,application)
        ViewModelProvider(this, viewModelFactory)[DetailEateryViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailEateryInforBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupToolBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapsFragment = childFragmentManager.findFragmentById(R.id.previewMapEatery) as SupportMapFragment?
        mapsFragment?.getMapAsync(this)
    }

    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = "Eatery Info"
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 24

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
            map.uiSettings.setAllGesturesEnabled(false)
            map.uiSettings.isMapToolbarEnabled = false
            val location = viewModel?.selectedProperty?.value?.addressEatery?.location
            if(location !== null){
                map.addMarker(MarkerOptions().position(location).title(getString(R.string.your_shipping_location_is_here)))
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location,
                        AddNewAddressFragment.DEFAULT_ZOOM
                    ))
            } else {
                findNavController().navigateUp()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        this.map = p0
        setupMap()
    }
}