package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.maxkeppeler.sheets.core.ButtonStyle
import com.maxkeppeler.sheets.info.InfoSheet
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.ProductAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentDetailEateryBinding
import xyz.daijoubuteam.foodshoppingapp.model.Eatery


class DetailEateryFragment : Fragment() {
    private lateinit var binding: FragmentDetailEateryBinding
    private lateinit var eateryProperty: Eatery
    private val viewModel: DetailEateryViewModel by lazy {
        val application = requireNotNull(activity).application
        eateryProperty = DetailEateryFragmentArgs.fromBundle(requireArguments()).eaterySelected
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
        handleClickButtonInfo()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun setupForYouProductListViewAdapter() {
        binding.forYouProductRecyclerView.adapter = ProductAdapter(ProductAdapter.OnClickListener{
            context?.let { it ->
                InfoSheet().show(it) {
                    title("Add Item")
                    displayNegativeButton(false)
                    positiveButtonStyle(ButtonStyle.NORMAL)
                    onPositive("Install") {
                        // Handle event
                    }
                    customView(R.layout.fragment_item_time_activity_eatery)
                }
            }
        })
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

    private fun handleClickButtonInfo() {
        binding.floatingActionInfo.setOnClickListener {
            this.findNavController().navigate(DetailEateryFragmentDirections.actionDetailEateryFragmentToDetailEateryInforFragment(eateryProperty))
        }
    }
}