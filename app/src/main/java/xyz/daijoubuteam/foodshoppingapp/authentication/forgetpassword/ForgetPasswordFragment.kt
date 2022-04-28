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
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentForgetPasswordBinding
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard

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
        setMessageObserver()
        setupHideKeyboard()

        return binding.root
    }

    private fun setSendResetPasswordEmailResultObserver() {
        viewmodel.sendResetPasswordEmailResult.observe(viewLifecycleOwner){
            it?.let {result ->
                if(result.isSuccess){
                    viewmodel.onShowMessage(getString(R.string.send_email_successful))
                } else {
                    viewmodel.onShowMessage(getString(R.string.send_email_failed))
                    Timber.e(it.exceptionOrNull()?.message.toString(), it.exceptionOrNull())
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

    private fun setMessageObserver(){
        viewmodel.message.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewmodel.onShowMessageComplete()
            }
        }
    }

    private fun setupHideKeyboard(){
        binding.forgetPasswordEmailTextInput.editText?.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowKeyboard()){
                hideKeyboard()
            }
        }
    }

    private fun shouldShowKeyboard(): Boolean {
        return binding.forgetPasswordEmailTextInput.editText?.hasFocus() == true
    }
}