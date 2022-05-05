package xyz.daijoubuteam.foodshoppingapp.client.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.CategoryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        setupPopularEateryListViewAdapter()
        setupCategoryListViewAdapter()
        //setupNearbyEateryListViewAdapter()
        setupOnAvatarClickListener()

        viewModel.navigateToSelectedEatery.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailEateryFragment(it))
                viewModel.onNavigateToSelectedEateryComplete()
            }
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun setupPopularEateryListViewAdapter() {
        binding.recyPopularEatery.adapter = EateryAdapter(EateryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyPopularEatery.adapter as EateryAdapter
        viewModel.eateryList.observe(viewLifecycleOwner) {
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

//    private fun setupNearbyEateryListViewAdapter() {
//        binding.recyNearByEatery.adapter = EateryAdapter()
//        val adapter = binding.recyNearByEatery.adapter as EateryAdapter
//        viewModel.eateryList.observe(viewLifecycleOwner) {
//            if (it != null) {
//                adapter.submitList(it)
//            }
//        }
//    }

    private fun setupOnAvatarClickListener(){
        binding.fragmentHomeAvatar.setOnClickListener {
            val activity = this.activity as? MainActivity
            activity?.setMenuSelectedItem(R.id.profileFragment)
        }
    }

    private fun hideActionBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }
}