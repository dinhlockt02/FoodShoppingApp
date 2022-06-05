package xyz.daijoubuteam.foodshoppingapp.client.home.categories_detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryCategoryAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentCategoryDetailBinding
import xyz.daijoubuteam.foodshoppingapp.model.Category

class CategoryDetailFragment : Fragment() {
    private lateinit var binding: FragmentCategoryDetailBinding
    private lateinit var categorySelected: Category
    private val viewModel: CategoryDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        categorySelected = CategoryDetailFragmentArgs.fromBundle(requireArguments()).category
        val viewModelFactory = CategoryDetailViewModelFactory(categorySelected, application)
        ViewModelProvider(this, viewModelFactory)[CategoryDetailViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupForEateryListAdapter()
        handleClickEateryItem ()
        setupToolBar()
        return binding.root
    }
    private fun setupForEateryListAdapter() {
        binding.recyEateryList.adapter = EateryCategoryAdapter(EateryCategoryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyEateryList.adapter as EateryCategoryAdapter
        viewModel.eateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    private fun handleClickEateryItem () {
        viewModel.navigateToSelectedEatery.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                findNavController().navigate(CategoryDetailFragmentDirections.actionCategoryDetailToDetailEateryFragment(it))
                viewModel.onNavigateToSelectedEateryComplete()
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setShowHideAnimationEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = categorySelected.name

    }
}