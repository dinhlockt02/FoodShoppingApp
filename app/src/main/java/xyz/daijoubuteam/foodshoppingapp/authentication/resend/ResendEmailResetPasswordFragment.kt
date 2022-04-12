package xyz.daijoubuteam.foodshoppingapp.authentication.resend

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
import androidx.navigation.fragment.navArgs
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.authentication.forgetpassword.ForgetPasswordFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentResendEmailResetPasswordBinding

class ResendEmailResetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResendEmailResetPasswordBinding
    private val args: ResendEmailResetPasswordFragmentArgs by navArgs()
    private val viewmodel: ResendEmailResetPasswordViewModel by lazy {
        val viewModelFactory = ResendEmailResetPasswordViewModelFactory(args.resetEmail)
        ViewModelProvider(this, viewModelFactory)[ResendEmailResetPasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_resend_email_reset_password, container, false)
        binding.viewmodel = viewmodel

        setNavigateToLoginObserver()
        setSendResetPasswordEmailResultObserver()

        return binding.root
    }

    private fun setNavigateToLoginObserver() {
        viewmodel.navigateToLogin.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(ResendEmailResetPasswordFragmentDirections.actionResendEmailResetPasswordFragmentToLoginFragment())
                viewmodel.onNavigateToLoginComplete()
            }
        }
    }

    private fun setSendResetPasswordEmailResultObserver() {
        viewmodel.sendResetPasswordEmailResult.observe(viewLifecycleOwner){
            it?.let {result ->
                if(result.isSuccess){
                    Toast.makeText(requireContext(), "Send email successful", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Send email failed", Toast.LENGTH_LONG).show()
                    Log.e("resend-email-password", it.exceptionOrNull()?.message.toString(), it.exceptionOrNull())
                }
                viewmodel.onSendResetPasswordEmailComplete()
            }
        }
    }

}