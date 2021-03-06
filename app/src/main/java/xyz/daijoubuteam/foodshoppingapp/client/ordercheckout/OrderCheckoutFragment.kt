package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.MainActivity
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
        setupToolBar()
        val adapter = BagOrderItemAdapter(BagOrderItemAdapter.OnClickListener{
            orderCheckOutViewModel.navigateToBagOrderItemFragment(it)
        })
        binding.listOrderItems.adapter = adapter
        orderCheckOutViewModel.orderItemList.observe(viewLifecycleOwner){
            if (it != null) {
                adapter.submitList(it)
                orderCheckOutViewModel.totalPriceCounting(it)
            }else{
                this.findNavController().navigateUp()
            }
        }
        orderCheckOutViewModel.navigateToOrderFragment.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(OrderCheckOutFragmentDirections.actionOrderCheckOutFragmentToOrdersFragment())
//                this.findNavController().navigateUp()
//                val activity = requireActivity() as? MainActivity
//                activity?.setMenuSelectedItem(R.id.ordersFragment)
                orderCheckOutViewModel.doneNavigateToOrderFragment()
            }
        })

        orderCheckOutViewModel.navigateToProfileAddressEditFragment.observe(viewLifecycleOwner,
            Observer {
                if(it == true){
                    this.findNavController().navigate(OrderCheckOutFragmentDirections.actionOrderCheckOutFragmentToProfileAddressEditFragment())
                    orderCheckOutViewModel.doneNavigateToProfileAddressEditFragment()
                }
            })

        orderCheckOutViewModel.navigateToDetailEateryFragment.observe(viewLifecycleOwner, Observer {
            if(it == true){
                if(orderCheckOutViewModel.eatery.value != null){
                    this.findNavController().navigate(OrderCheckOutFragmentDirections.actionOrderCheckOutFragmentToDetailEateryFragment(orderCheckOutViewModel.eatery.value!!))
                }
                orderCheckOutViewModel.doneNavigateToDetailEateryFragment()
            }
        })

        orderCheckOutViewModel.navigateToBagOrderItemFragment.observe(viewLifecycleOwner, Observer {
            if(it !== null){
                this.findNavController().navigate(OrderCheckOutFragmentDirections.actionOrderCheckOutFragmentToBagOrderItemFragment(orderCheckOutViewModel.orderId, it.productId!!.path, it.quantity!!))
                orderCheckOutViewModel.doneNavigateToBagOrderItemFragment()
            }
        })

        orderCheckOutViewModel.message.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty() ){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                orderCheckOutViewModel.onShowMessageComplete()
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupToolBar()
    }

    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = "Order Information"
    }
}