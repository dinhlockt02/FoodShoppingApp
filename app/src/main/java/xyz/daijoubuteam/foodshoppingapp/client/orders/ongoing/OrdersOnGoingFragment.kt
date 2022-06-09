package xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.bag.BagViewModel
import xyz.daijoubuteam.foodshoppingapp.client.bag.BagViewModelFactory
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrderAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentBagBinding
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersOnGoingBinding

class OrdersOnGoingFragment : Fragment() {

    private val onGoingViewModel: OrdersOnGoingViewModel by lazy{
        val factory = OrdersOnGoingViewModelFactory()
        ViewModelProvider(this, factory)[OrdersOnGoingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentOrdersOnGoingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_on_going, container, false)
        binding.viewModel = onGoingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = OrderAdapter()
        binding.ordersOnGoingRecycleView.adapter = adapter

        onGoingViewModel.orderList.observe(viewLifecycleOwner){
            if(it.isNotEmpty() && it != null) {
                adapter.submitList(it)
            }
        }

        return binding.root
    }



}