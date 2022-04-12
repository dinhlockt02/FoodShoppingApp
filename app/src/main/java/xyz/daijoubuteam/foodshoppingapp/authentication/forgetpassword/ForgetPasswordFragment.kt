package xyz.daijoubuteam.foodshoppingapp.authentication.forgetpassword

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgetPasswordBinding
    private val viewmodel by lazy {
        val viewModelFactory = ForgetPasswordViewModelFactory()
        ViewModelProvider(this, viewModelFactory).get(ForgetPasswordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_forget_password,
                container,
                false)

        binding.viewmodel = viewmodel

        setNavigateToLoginObserver()
        setSendResetPasswordEmailResultObserver()

        return binding.root
    }

    private fun setSendResetPasswordEmailResultObserver() {
        viewmodel.sendResetPasswordEmailResult.observe(viewLifecycleOwner){
            it?.let {result ->
                if(result.isSuccess){
                    Toast.makeText(requireContext(), "Send email successful", Toast.LENGTH_LONG).show()
                    findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToResendEmailResetPasswordFragment(viewmodel.email.value!!))
                } else {
                    Toast.makeText(requireContext(), "Send email failed", Toast.LENGTH_LONG).show()
                    Log.e("forget-password", it.exceptionOrNull()?.message.toString(), it.exceptionOrNull())
                }
                viewmodel.onSendResetPasswordEmailComplete()
            }
        }
    }

    private fun setNavigateToLoginObserver() {
        viewmodel.navigateToLogin.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToLoginFragment())
                viewmodel.onNavigateToLoginComplete()
            }
        }
    }
}