package xyz.daijoubuteam.foodshoppingapp.client.bag

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.client.orders.OrderApdater
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentBagBinding

class BagFragment : Fragment() {

    private val bagViewModel: BagViewModel by lazy{
        val factory = BagViewModelFactory()
        ViewModelProvider(this, factory)[BagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBagBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bag, container, false)
        binding.bagViewModel = bagViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = OrderApdater()
        binding.orderList.adapter = adapter

        bagViewModel.orderList.observe(viewLifecycleOwner) {
            if(it.isNotEmpty() && it != null) {
                Timber.i(it.first().eatery.toString())
                adapter.submitList(it)
            }
        }
        return  binding.root
    }

}