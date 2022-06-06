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


class OrderCheckOutFragment : Fragment() {
    private val orderCheckOutViewModel: OrderCheckOutViewModel by lazy{
        val orderId = OrderCheckOutFragmentArgs.fromBundle(requireArguments()).orderId
        val factory = OrderCheckOutViewModelFactory(orderId)
        ViewModelProvider(this, factory)[OrderCheckOutViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOrderCheckoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_checkout, container, false)
        binding.viewModel = orderCheckOutViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = OrderItemAdapter()
        binding.listOrderItems.adapter = adapter
        orderCheckOutViewModel.orderItemList.observe(viewLifecycleOwner){
            if (it != null){
                adapter.submitList(it)
                orderCheckOutViewModel.totalPriceCounting(it)
            }
        }
        return binding.root
    }

}