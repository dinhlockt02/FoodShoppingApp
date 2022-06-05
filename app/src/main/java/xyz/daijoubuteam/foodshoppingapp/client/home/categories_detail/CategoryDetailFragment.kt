package xyz.daijoubuteam.foodshoppingapp.client.home.categories_detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryCategoryAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentCategoryDetailBinding
import xyz.daijoubuteam.foodshoppingapp.model.Category
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

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
        setupToolBar()
        return binding.root
    }
    private fun setupForEateryListAdapter() {
        binding.recyEateryList.adapter = EateryCategoryAdapter()
        val adapter = binding.recyEateryList.adapter as EateryCategoryAdapter
        viewModel.eateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setShowHideAnimationEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = categorySelected.name

    }
}