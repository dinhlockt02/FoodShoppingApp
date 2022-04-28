package xyz.daijoubuteam.foodshoppingapp.client.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentDetailEateryBinding

class DetailEateryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val binding =  FragmentDetailEateryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val eateryProperty = DetailEateryFragmentArgs.fromBundle(requireArguments()).eaterySelected
        val viewModelFactory = DetailEateryViewModelFactory(eateryProperty,application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(DetailEateryViewModel::class.java)
        return binding.root
    }
}