package xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import xyz.daijoubuteam.foodshoppingapp.adapter.AddressAdapter
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentProfileAddressEditBinding
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard

class ProfileAddressEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileAddressEditBinding
    private val viewmodel: ProfileAddressEditViewModel by lazy {
        val factory = ProfileAddressEditViewModelFactory()
        ViewModelProvider(this, factory)[ProfileAddressEditViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileAddressEditBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        setupErrorSnackbar()
        setupSoftKeyboardUI()
        setupOnAddNewAddressButtonClicked()
        setupAddressRecyclerViewAdapter()
        return binding.root
    }

    private fun setupAddressRecyclerViewAdapter() {
        binding.profileEditAddressRecyclerView.adapter = AddressAdapter()
        val adapter = binding.profileEditAddressRecyclerView.adapter as AddressAdapter
        viewmodel.user.observe(viewLifecycleOwner) {
            if (it !== null) {
                adapter.submitList(it.shippingAddresses)
            }
        }
    }

    private fun setupOnAddNewAddressButtonClicked() {
        binding.profileEditAddressAddMoreButton.setOnClickListener {
            val action =
                ProfileAddressEditFragmentDirections.actionProfileAddressEditFragmentToSelectLocationFragment()
            findNavController().navigate(action)
        }
    }


    private fun setupErrorSnackbar() {
        viewmodel.message.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty() && it.isNotBlank()) {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSoftKeyboardUI() {
        binding.profileEditFirstNameTextField.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }
        binding.profileEditLastNameTextField.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }
        binding.profileEditPhoneNumber.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }
    }
}