package xyz.daijoubuteam.foodshoppingapp.client.home

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.MainApplication
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CarouselAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CategoryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentHomeBinding
import java.lang.reflect.Type
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    var location: Location? = null

    private val viewModel: HomeViewModel by lazy {
        val factory = HomeViewModelFactory()
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        hideActionBar()
        handleUserLocation()
        setupOnAvatarClickListener()
        setupPopularEateryListViewAdapter()
        setupNearbyEateryListViewAdapter()
        setupCategoryListViewAdapter()
        handleClickEateryItem()
        handleClickBtnViewAllEatery()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }
    private fun hideActionBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }
    private fun getCityName(lat: Double,long: Double):String{
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,long,3)
        return address[0].getAddressLine(0)
    }
    private fun handleUserLocation() {
        // location user
        val mainApplication = activity?.application as? MainApplication

        if(mainApplication?.location?.value != null) {
            location = mainApplication.location.value
        }
        mainApplication?.location?.observe(viewLifecycleOwner){
            Timber.i(it.toString())
            if(it != null) {
                location = it
                binding.txtUserAddress.text =
                    location?.let { getCityName(it.latitude, it.longitude) }
            }
        }
    }
    private fun setupOnAvatarClickListener(){
        binding.userAvatar.setOnClickListener {
            val activity = this.activity as? MainActivity
            activity?.setMenuSelectedItem(R.id.profileFragment)
        }
    }
    private fun setupPopularEateryListViewAdapter() {
        binding.recyPopularEatery.adapter = EateryAdapter(EateryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyPopularEatery.adapter as EateryAdapter
        viewModel.popularEateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
    private fun setupCategoryListViewAdapter() {
        binding.recyCategories.adapter = CategoryAdapter()
        val adapter = binding.recyCategories.adapter as CategoryAdapter
        viewModel.categoryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
    private fun setupNearbyEateryListViewAdapter() {
        binding.recyNearByEatery.adapter = EateryAdapter(EateryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyNearByEatery.adapter as EateryAdapter
        viewModel.eateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
    private fun handleClickEateryItem() {
        viewModel.navigateToSelectedEatery.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailEateryFragment(it))
                viewModel.onNavigateToSelectedEateryComplete()
            }
        })
    }
    private fun handleClickBtnViewAllEatery() {
        binding.btnPopularEateries.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVerticalListEateryFragment(TypesViewAll.POPULAR))
        }
        binding.btnNearbyEateries.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVerticalListEateryFragment(TypesViewAll.NEARBY))
        }
    }
//    private fun setupCarouselListViewAdapter() {
//        viewModel.carouselEventList.observe(viewLifecycleOwner) {
//            if (it != null) {
//                binding.myPager.adapter = SlideAdapter(this.requireContext(), it)
//            }
//        }
//        binding.myTablayout.setupWithViewPager(binding.myPager)
//    }

//    private fun setupNearbyEateryListViewAdapter() {
//        binding.recyNearByEatery.adapter = EateryAdapter()
//        val adapter = binding.recyNearByEatery.adapter as EateryAdapter
//        viewModel.eateryList.observe(viewLifecycleOwner) {
//            if (it != null) {
//                adapter.submitList(it)
//            }
//        }
//    }
}