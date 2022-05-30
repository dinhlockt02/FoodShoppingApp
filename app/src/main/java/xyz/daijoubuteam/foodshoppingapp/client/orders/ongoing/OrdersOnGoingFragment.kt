package xyz.daijoubuteam.foodshoppingapp.client.orders.ongoing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrderApdater
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrdersOnGoingBinding
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.repositories.UserRepository

class OrdersOnGoingFragment : Fragment() {

    private lateinit var binding: FragmentOrdersOnGoingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_on_going, container, false)

        binding.ordersOnGoingRecycleView.adapter = OrderApdater()
        val adapter = binding.ordersOnGoingRecycleView.adapter as OrderApdater
        //adapter.submitList(ordersList)

        return binding.root
    }



}