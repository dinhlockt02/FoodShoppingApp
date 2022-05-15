package xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrdersApdater
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersOnGoingBinding
import xyz.daijoubuteam.foodshoppingapp.model.Order

class OrdersOnGoingFragment : Fragment() {

    private lateinit var binding: FragmentOrdersOnGoingBinding
    private var ordersList = arrayListOf<Order>(
        Order("1"),
        Order("2"),
        Order("3"),
        Order("4"),
        Order("5"),
        Order("6"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_on_going, container, false)

        binding.ordersOnGoingRecycleView.adapter = OrdersApdater()
        val adapter = binding.ordersOnGoingRecycleView.adapter as OrdersApdater
        adapter.submitList(ordersList)

        return binding.root
    }



}