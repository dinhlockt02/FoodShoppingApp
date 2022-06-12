package xyz.daijoubuteam.foodshoppingapp.client.orders.orderInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.ordercheckout.OrderCheckOutFragmentArgs
import xyz.daijoubuteam.foodshoppingapp.client.ordercheckout.OrderCheckOutViewModel
import xyz.daijoubuteam.foodshoppingapp.client.ordercheckout.OrderCheckOutViewModelFactory
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrderItemAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentOrderInformationBinding

class OrderInformationFragment : Fragment() {
    private val orderInformationViewModel: OrderInformationViewModel by lazy{
        val orderId = OrderInformationFragmentArgs.fromBundle(requireArguments()).orderId
        val factory = OrderInformationViewModelFactory(orderId)
        ViewModelProvider(this, factory)[OrderInformationViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentOrderInformationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_order_information, container, false)
        binding.viewModel = orderInformationViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = OrderItemAdapter()
        binding.listOrderItems.adapter = adapter
        orderInformationViewModel.order.observe(viewLifecycleOwner, Observer {
            if(it != null && !it.orderItems.isNullOrEmpty()){
                adapter.submitList(it.orderItems)
            }
        })
        orderInformationViewModel.navigateUpToOrderFragment.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigateUp()
                orderInformationViewModel.doneNavigateUpToOrderFragment()
            }
        })
        orderInformationViewModel.navigateToEateryDetailFragment.observe(viewLifecycleOwner, Observer {
            if(it == true){
                if(orderInformationViewModel.eatery.value != null){
                    this.findNavController().navigate(OrderInformationFragmentDirections.actionOrderInformationToDetailEateryFragment(orderInformationViewModel.eatery.value!!))
                }else{
                    orderInformationViewModel.messageCantFindEatery()
                }
                orderInformationViewModel.doneNavigateToEateryDetailFragment()
            }
        })

        orderInformationViewModel.message.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty() ){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                orderInformationViewModel.onShowMessageComplete()
            }
        }

        return binding.root
    }

}