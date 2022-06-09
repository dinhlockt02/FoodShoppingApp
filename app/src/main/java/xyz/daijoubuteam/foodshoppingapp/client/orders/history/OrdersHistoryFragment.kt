package xyz.daijoubuteam.foodshoppingapp.client.orders.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firestore.v1.StructuredQuery
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrderAdapter
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrdersFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingViewModel
import xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing.OrdersOnGoingViewModelFactory
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersHistoryBinding

class OrdersHistoryFragment : Fragment() {
    private val historyViewModel: OrdersHistoryViewModel by lazy{
        val factory = OrdersHistoryViewModelFactory()
        ViewModelProvider(this, factory)[OrdersHistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentOrdersHistoryBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_orders_history, container, false)
        binding.viewModel = historyViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = OrderAdapter(OrderAdapter.OnClickListener{
            historyViewModel.navigateToOrderInfoFragment(it)
        })
        binding.ordersHistoryRecycleView.adapter = adapter

        historyViewModel.orderList.observe(viewLifecycleOwner){
            if(it!= null){
                adapter.submitList(it)
            }
        }

        historyViewModel.navigateToOrderInfoFragment.observe(viewLifecycleOwner, Observer {
            if(it!= null){
                this.findNavController().navigate(OrdersFragmentDirections.actionOrdersFragmentToOrderInformation(it.id!!))
                historyViewModel.doneNavigateToOrderInfoFragment()
            }
        })

        return binding.root
    }
}