package xyz.daijoubuteam.foodshoppingapp.client.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewmodel: ProfileViewModel by lazy {
        val factory = ProfileViewModelFactory()
        ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        setupNavigateToProfileAndAddressFragment()
        setupMessageSnackbar()
        setupNotificationLayoutClickListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val supportActionBar = (requireActivity() as? MainActivity)?.supportActionBar
        Timber.i(supportActionBar.toString())
        supportActionBar?.hide()
    }


    private fun setupNotificationLayoutClickListener() {
        binding.notificationLayout.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToNotificationListFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupNavigateToProfileAndAddressFragment() {
        binding.btnProfileAndAddress.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToProfileAddressEditFragment()
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
}