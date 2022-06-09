package xyz.daijoubuteam.foodshoppingapp.client.orders.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrderAdapter
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrdersFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingViewModel
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingViewModelFactory
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersOnGoingBinding
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersUpComingBinding


class OrdersUpComingFragment : Fragment() {
    private val upComingViewModel: OrdersUpComingViewModel by lazy{
        val factory = OrdersUpComingViewModelFactory()
        ViewModelProvider(this, factory)[OrdersUpComingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOrdersUpComingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_orders_up_coming, container, false)
        binding.viewModel = upComingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = OrderAdapter(OrderAdapter.OnClickListener{
            upComingViewModel.navigateToOrderInfoFragment(it)
        })
        binding.ordersUpComingRecycleView.adapter = adapter

        upComingViewModel.orderList.observe(viewLifecycleOwner){
            if(it!= null){
                adapter.submitList(it)
            }
        }

        upComingViewModel.navigateToOrderInfoFragment.observe(viewLifecycleOwner, Observer {
            if(it!= null){
                this.findNavController().navigate(OrdersFragmentDirections.actionOrdersFragmentToOrderInformation(it.id!!))
                upComingViewModel.doneNavigateToOrderInfoFragment()
            }
        })

        return binding.root
    }

}