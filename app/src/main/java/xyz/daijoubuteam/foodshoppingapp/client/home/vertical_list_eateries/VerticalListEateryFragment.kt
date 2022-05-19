package xyz.daijoubuteam.foodshoppingapp.client.home.vertical_list_eateries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.home.HomeFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.client.home.TypesViewAll
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.VerticalCardEateryAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentVerticalListEateryBinding

class VerticalListEateryFragment : Fragment() {
    private lateinit var binding: FragmentVerticalListEateryBinding
    private lateinit var typeEateryList: TypesViewAll
    private val viewModel: VerticalListEateryViewModel by lazy {
        val application = requireNotNull(activity).application
        typeEateryList = VerticalListEateryFragmentArgs.fromBundle(requireArguments()).type
        val viewModelFactory = VerticalListEateryViewModelFactory(typeEateryList, application)
        ViewModelProvider(this, viewModelFactory)[VerticalListEateryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerticalListEateryBinding.inflate(inflater)
        binding.viewModel = viewModel
        setupToolBar()
        if(viewModel.typeList == TypesViewAll.POPULAR) {
            setupPopularEateryListViewAdapter()
        } else {
            setupNearbyEateryListViewAdapter()
        }

        handleClickEateryItem()
        return binding.root
    }
    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = if(viewModel.typeList == TypesViewAll.POPULAR) "POPULAR" else "NEARBY"
    }
    private fun setupPopularEateryListViewAdapter() {
        binding.recyVerticalPopularEatery.adapter = VerticalCardEateryAdapter(VerticalCardEateryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyVerticalPopularEatery.adapter as VerticalCardEateryAdapter
        viewModel.popularEateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
    private fun setupNearbyEateryListViewAdapter() {
        binding.recyVerticalPopularEatery.adapter = VerticalCardEateryAdapter(VerticalCardEateryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyVerticalPopularEatery.adapter as VerticalCardEateryAdapter
        viewModel.nearlyEateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
    private fun handleClickEateryItem() {
        viewModel.navigateToSelectedEatery.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                this.findNavController().navigate(VerticalListEateryFragmentDirections.actionVerticalListEateryFragmentToDetailEateryFragment(it))
                viewModel.onNavigateToSelectedEateryComplete()
            }
        })
    }
}