package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrderCheckoutBinding
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Order

class OrderCheckOutFragment : Fragment() {

    private lateinit var orderProperty: Order
    private val orderCheckOutViewModel: OrderCheckOutViewModel by lazy{
        val application = requireNotNull(activity).application
        orderProperty = OrderCheckOutFragmentArgs.fromBundle(requireArguments()).orderSelected
        val factory = OrderCheckOutViewModelFactory(orderProperty, application)
        ViewModelProvider(this, factory)[OrderCheckOutViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOrderCheckoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_checkout, container, false)
        binding.viewModel = orderCheckOutViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = OrderCheckOutAdapter()
        binding.listOrderItems.adapter = adapter
        orderCheckOutViewModel.selectedProperty.observe(viewLifecycleOwner){
            if (it != null){
                adapter.submitList(it.orderItems)
            }
        }
        return binding.root
    }
}