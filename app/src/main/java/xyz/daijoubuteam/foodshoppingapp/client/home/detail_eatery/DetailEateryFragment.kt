package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.ProductAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentDetailEateryBinding


class DetailEateryFragment : Fragment() {
    private lateinit var binding: xyz.daijoubuteam.foodshoppingapp.databinding.FragmentDetailEateryBinding
    private val viewModel: DetailEateryViewModel by lazy {
        val application = requireNotNull(activity).application
        val eateryProperty = DetailEateryFragmentArgs.fromBundle(requireArguments()).eaterySelected
        val viewModelFactory = DetailEateryViewModelFactory(eateryProperty,application)
        ViewModelProvider(this, viewModelFactory)[DetailEateryViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =  FragmentDetailEateryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupForYouProductListViewAdapter()
        handleClickButtonHome()
        return binding.root
    }

    private fun setupForYouProductListViewAdapter() {
        binding.forYouProductRecyclerView.adapter = ProductAdapter()
        val adapter = binding.forYouProductRecyclerView.adapter as ProductAdapter
        viewModel.selectedProperty.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it.products)
            }
        }
    }

    private fun handleClickButtonHome() {
        binding.floatingActionButtonHome.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}