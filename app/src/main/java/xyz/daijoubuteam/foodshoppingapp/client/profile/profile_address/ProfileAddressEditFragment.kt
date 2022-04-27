package xyz.daijoubuteam.foodshoppingapp.client.profile.profile_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
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

        setupMessageSnackbar()
        setupSoftKeyboardUI()
        setupOnAddNewAddressButtonClicked()
        setupAddressRecyclerViewAdapter()
        setupOnProfileEditUserAvatarClick()

        return binding.root
    }

    private fun setupAddressRecyclerViewAdapter() {
        binding.profileEditAddressRecyclerView.adapter =
            AddressAdapter(AddressAdapter.OnClickListener {
                val action =
                    ProfileAddressEditFragmentDirections.actionProfileAddressEditFragmentToAddNewAddressFragment(
                        editAddress = it
                    )
                findNavController().navigate(action)
            })
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


    private fun setupMessageSnackbar() {
        viewmodel.message.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty() && it.isNotBlank()) {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSoftKeyboardUI() {
        binding.profileEditFirstNameTextField.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!shouldShowSoftKeyboard()) {
                hideKeyboard()
            }
        }
        binding.profileEditLastNameTextField.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!shouldShowSoftKeyboard()) {
                hideKeyboard()
            }
        }
        binding.profileEditPhoneNumber.editText?.setOnFocusChangeListener { view, hasFocus ->
            if (!shouldShowSoftKeyboard()) {
                hideKeyboard()
            }
        }
    }

    private fun shouldShowSoftKeyboard(): Boolean {
        return binding.profileEditFirstNameTextField.editText?.hasFocus() == true ||
                binding.profileEditLastNameTextField.editText?.hasFocus() == true ||
                binding.profileEditPhoneNumber.editText?.hasFocus() == true
    }

    private fun setupOnProfileEditUserAvatarClick() {
        binding.profileEditUserAvatar.setOnClickListener {
            startCrop()
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            binding.profileEditUserAvatar.setImageURI(uriContent)
            if (uriContent != null) {
                viewmodel.uploadUserAvatar(uriContent)
            }

        } else {
            viewmodel.onShowMessage(result.error?.message)
        }
    }

    private fun startCrop() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setCropShape(CropImageView.CropShape.OVAL)
                setFixAspectRatio(true)
            }
        )
    }
}