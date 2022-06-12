package xyz.daijoubuteam.foodshoppingapp.client.bag.bagOrderItem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.ordercheckout.OrderCheckOutViewModel
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentBagOrderItemBinding

class BagOrderItemFragment : Fragment() {

    private val bagOrderItemViewModel: BagOrderItemViewModel by lazy {
        val orderId = BagOrderItemFragmentArgs.fromBundle(requireArguments()).orderId
        val productId = BagOrderItemFragmentArgs.fromBundle(requireArguments()).productId
        val quantity = BagOrderItemFragmentArgs.fromBundle(requireArguments()).quantity
        val factory = BagOrderItemViewModelFactory(orderId,productId,quantity)
        ViewModelProvider(this, factory)[BagOrderItemViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBagOrderItemBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_bag_order_item, container, false)
        binding.viewModel = bagOrderItemViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        bagOrderItemViewModel.product.observe(viewLifecycleOwner){
            bagOrderItemViewModel.setTotalPrice()
        }
        bagOrderItemViewModel.orderQuantity.observe(viewLifecycleOwner){
            bagOrderItemViewModel.setTotalPrice()
        }
        return binding.root
    }

}