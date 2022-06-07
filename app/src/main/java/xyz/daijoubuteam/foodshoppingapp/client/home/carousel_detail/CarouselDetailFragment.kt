package xyz.daijoubuteam.foodshoppingapp.client.home.carousel_detail

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
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.EventEateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.adapter.VerticalCardEateryAdapter
import xyz.daijoubuteam.foodshoppingapp.client.home.vertical_list_eateries.VerticalListEateryFragmentArgs
import xyz.daijoubuteam.foodshoppingapp.client.home.vertical_list_eateries.VerticalListEateryViewModel
import xyz.daijoubuteam.foodshoppingapp.client.home.vertical_list_eateries.VerticalListEateryViewModelFactory
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentCarouselDetailBinding
import xyz.daijoubuteam.foodshoppingapp.model.Event

class CarouselDetailFragment : Fragment() {
    private lateinit var binding: FragmentCarouselDetailBinding
    private lateinit var eventSelected: Event
    private val viewModel: CarouselDetailViewModel by lazy {
        val application = requireNotNull(activity).application
        eventSelected = CarouselDetailFragmentArgs.fromBundle(requireArguments()).event
        val viewModelFactory = CarouselDetailViewModelFactory(eventSelected, application)
        ViewModelProvider(this, viewModelFactory)[CarouselDetailViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCarouselDetailBinding.inflate(inflater)
        binding.viewModel = viewModel
        setupToolBar()
        setupPopularEateryListViewAdapter()
        handleClickEateryItem()
        return binding.root
    }
    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = eventSelected.name
    }
    private fun setupPopularEateryListViewAdapter() {
        binding.recyEateryList.adapter =    EventEateryAdapter(
            EventEateryAdapter.OnClickListener{
            viewModel.displayPropertyDetailEatery(it)
        })
        val adapter = binding.recyEateryList.adapter as EventEateryAdapter
        viewModel.eateryList.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }
    private fun handleClickEateryItem() {
        viewModel.navigateToSelectedEatery.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                findNavController().navigate(CarouselDetailFragmentDirections.actionCarouselDetailToDetailEateryFragment(it))
                viewModel.onNavigateToSelectedEateryComplete()
            }
        })
    }
}