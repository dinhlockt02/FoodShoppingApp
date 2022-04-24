package xyz.daijoubuteam.foodshoppingapp.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentLoginBinding
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewmodel: LoginViewModel by lazy {
        val viewModelFactory = LoginViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        setNavigateToSignUpObserver()
        setNavigateToForgetPasswordObserver()
        setLoginResultObserver()
        setLoginWithGoogleButton()
        setupSoftKeyboardUI()
        setLoginWithFacebookButton()

        return binding.root
    }

    private fun setLoginWithFacebookButton() {
        binding.loginWithFacebookButton.setOnClickListener {
            hideKeyboard()
            Snackbar.make(
                this.requireView(),
                "Hội những người anti facebook",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun setNavigateToForgetPasswordObserver() {
        viewmodel.navigateToForgetPassword.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment())
                viewmodel.onNavigateToForgetPasswordComplete()
            }
        }
    }

    private fun setLoginWithGoogleButton() {
        binding.loginWithGoogleButton.setOnClickListener {
            signIn()
        }
    }

    private fun setNavigateToSignUpObserver() {
        viewmodel.navigateToSignUp.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                viewmodel.onNavigateToSignupComplete()
            }
        }
    }

    private fun setLoginResultObserver() {
        viewmodel.loginResult.observe(viewLifecycleOwner) {
            it?.let { result ->
                if (result.isSuccess) {
                    viewmodel.onLoginComplete()
                } else if (result.isFailure) {
                    hideKeyboard()
                    Snackbar.make(
                        this.requireView(),
                        "${result.exceptionOrNull()?.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewmodel.onLoginComplete()
                }
            }
        }
    }


    private fun signIn(){
        val request = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()
        Identity.getSignInClient(requireActivity())
            .getSignInIntent(request)
            .addOnSuccessListener { pendingIntent ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                    activityResultLauncher.launch(intentSenderRequest)
                } catch (exception: Exception){
                    hideKeyboard()
                    Snackbar.make(
                        this.requireView(),
                        "\"Couldn't start One Tap UI: ${exception.localizedMessage}\"",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Timber.i("\"Couldn't start One Tap UI: ${exception.localizedMessage}\"")
                }
            }
            .addOnFailureListener {
                hideKeyboard()
                Snackbar.make(
                    this.requireView(),
                    "\"Couldn't start One Tap UI: ${it.localizedMessage}\"",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val credential = Identity.getSignInClient(requireActivity()).getSignInCredentialFromIntent(it.data)
            val idToken = credential.googleIdToken
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            viewmodel.onLoginWithGoogle(firebaseCredential)
        }
    }


    private fun setupSoftKeyboardUI(){
        binding.etEmail.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                hideKeyboard()
            }
        }
        binding.etPassword.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                hideKeyboard()
            }
        }

    }
}
