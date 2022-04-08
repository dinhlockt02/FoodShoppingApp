package xyz.daijoubuteam.foodshoppingapp.authentication.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.authentication.login.LoginFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.authentication.login.LoginViewModel
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentLoginBinding
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewmodel: SignUpViewModel by lazy {
        val viewModelFactory = SignUpViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                viewmodel.onNavigateToLoginComplete()
            }
        })
        return binding.root
    }
}