package xyz.daijoubuteam.foodshoppingapp.client.home.detail_eatery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentDetailEateryInforBinding

class DetailEateryInforFragment : Fragment() {
    private lateinit var binding: FragmentDetailEateryInforBinding
    private val viewModel: DetailEateryViewModel by lazy {
        val application = requireNotNull(activity).application
        val eateryProperty = DetailEateryInforFragmentArgs.fromBundle(requireArguments()).eatery
        val viewModelFactory = DetailEateryViewModelFactory(eateryProperty,application)
        ViewModelProvider(this, viewModelFactory)[DetailEateryViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailEateryInforBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupToolBar()
        return inflater.inflate(R.layout.fragment_detail_eatery_infor, container, false)
    }

    private fun setupToolBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.show()
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = "Eatery Info"
    }
}