package xyz.daijoubuteam.foodshoppingapp.client.home.product_to_bag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentProductToBagBinding
import xyz.daijoubuteam.foodshoppingapp.model.Product

class ProductToBagFragment : Fragment() {
    private lateinit var binding: FragmentProductToBagBinding
    private lateinit var productProperty: Product
    private var quantityItem: Int = 0
    private var totalPrice: Number = 0

    private val viewModel: ProductToBagViewModel by lazy {
        val application = requireNotNull(activity).application
        productProperty = ProductToBagFragmentArgs.fromBundle(requireArguments()).product
        val viewModelFactory = ProductToBagViewModelFactory(productProperty,application)
        ViewModelProvider(this, viewModelFactory)[ProductToBagViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductToBagBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setTextViews()
        handleClickButtonHome()
        handleClickQuantity()
        return binding.root
    }

    private fun setTextViews() {
        binding.tvQuatityProduct.text = quantityItem.toString()
        binding.tvTotalMoney.text = totalPrice.toString()
    }

    private fun handleClickButtonHome() {
        binding.floatingActionButtonHome.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun handleClickQuantity() {
        binding.btnPlus.setOnClickListener {
            quantityItem += 1
            totalPrice = quantityItem * productProperty.price!!
            setTextViews()
        }
        binding.btnSub.setOnClickListener {
            quantityItem -= 1
            totalPrice = quantityItem * productProperty.price!!
            setTextViews()
        }
    }

}