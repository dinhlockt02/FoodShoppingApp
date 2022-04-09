package xyz.daijoubuteam.foodshoppingapp.authentication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.signup.SignUpFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewmodel: LoginViewModel by lazy {
        val viewModelFactory = LoginViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        viewmodel.navigateToSignUp.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                viewmodel.onNavigateToSignupComplete()
            }
        })

        viewmodel.loginResult.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                if(result.isSuccess){
                    viewmodel.onLoginWithEmailAndPasswordComplete()
                    loginSuccessful()
                }else if (result.isFailure) {
                    Toast.makeText(this.requireContext(), "${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                    viewmodel.onLoginWithEmailAndPasswordComplete()
                }
            }
        })

        autoLogin()

        return binding.root
    }

    private fun autoLogin(){
        if(viewmodel.firebaseUser != null) {
            loginSuccessful()
        }
    }

    private fun loginSuccessful() {
        val intent = Intent(activity, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("USER", viewmodel.firebaseUser)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}