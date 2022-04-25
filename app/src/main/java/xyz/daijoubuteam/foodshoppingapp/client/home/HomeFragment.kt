package xyz.daijoubuteam.foodshoppingapp.client.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.adapter.AddressAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CategoryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CuisineAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.SlideAdapter
import xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address.ProfileAddressEditViewModel
import xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address.ProfileAddressEditViewModelFactory
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentHomeBinding
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Cuisine
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.SlideItem
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewmodel: HomeViewModel by lazy {
        val factory = HomeViewModelFactory()
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        setupPopularEateryListViewAdapter()
        setupCategoryListViewAdapter()
        setupNearbyEateryListViewAdapter()
        setupOnAvatarClickListener()
        return binding.root
    }

    private fun setupPopularEateryListViewAdapter() {
        binding.recyPopularEatery.adapter = EateryAdapter()
        val adapter = binding.recyPopularEatery.adapter as EateryAdapter
        viewmodel.eateryList.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "eateries list changed", Toast.LENGTH_LONG).show()
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    private fun setupCategoryListViewAdapter() {
        binding.recyCategories.adapter = CategoryAdapter()
        val adapter = binding.recyCategories.adapter as CategoryAdapter
        viewmodel.categoryList.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), "category list changed", Toast.LENGTH_LONG).show()
                adapter.submitList(it)
            }
        }
    }

    private fun setupNearbyEateryListViewAdapter() {
        binding.recyNearByEatery.adapter = EateryAdapter()
        val adapter = binding.recyNearByEatery.adapter as EateryAdapter
        viewmodel.eateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    private fun setupOnAvatarClickListener(){
        binding.fragmentHomeAvatar.setOnClickListener {
            val activity = this.activity as? MainActivity
            activity?.setMenuSelectedItem(R.id.profileFragment)
        }
    }
}