package xyz.daijoubuteam.foodshoppingapp.client.home.product_to_bag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentProductToBagBinding
import xyz.daijoubuteam.foodshoppingapp.model.Product

class ProductToBagFragment : Fragment() {
    private lateinit var binding: FragmentProductToBagBinding
    private lateinit var productProperty: Product
    private lateinit var eateryId:String
    private val viewModel: ProductToBagViewModel by lazy {
        val application = requireNotNull(activity).application
        productProperty = ProductToBagFragmentArgs.fromBundle(requireArguments()).product
        eateryId = ProductToBagFragmentArgs.fromBundle(requireArguments()).eateryId
        val viewModelFactory = ProductToBagViewModelFactory(eateryId,productProperty,application)
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
        handleSubmitOrder()
        messageObserver()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun setTextViews() {
        binding.tvQuatityProduct.text = viewModel.quantityItem.toString()
        binding.tvTotalMoney.text = viewModel.totalPrice.toString()
    }

    private fun handleClickButtonHome() {
        binding.floatingActionButtonHome.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun handleClickQuantity() {
        binding.btnPlus.setOnClickListener {
            viewModel.handleRaiseQuantityItem()
        }
        binding.btnSub.setOnClickListener {
            viewModel.handleMinusQuantityItem()
        }
    }

    private fun handleSubmitOrder() {
        binding.btnAddToCard.setOnClickListener {
            viewModel.handleBtnAddToBag()
        }
    }


    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty() ){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                this.findNavController().navigateUp()
                viewModel.onShowMessageComplete()
            }
        }
    }
}