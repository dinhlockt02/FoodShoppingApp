package xyz.daijoubuteam.foodshoppingapp.authentication.signup

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentSignUpBinding
import xyz.daijoubuteam.foodshoppingapp.utils.hideKeyboard

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewmodel: SignUpViewModel by lazy {
        val viewModelFactory = SignUpViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        setNavigateToLoginObserver()
        setSignUpResultObserver()
        setLoginWithGoogleButton()
        setupSoftKeyboardUI()

        return binding.root
    }


    private fun setNavigateToLoginObserver() {
        viewmodel.navigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                viewmodel.onNavigateToLoginComplete()
            }
        }
    }

    private fun setSignUpResultObserver() {
        viewmodel.signUpResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (result.isSuccess) {
                    hideKeyboard()
                    Snackbar.make(
                        this.requireView(),
                        "Đăng ký thành công.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (result.isFailure) {
                    hideKeyboard()
                    Snackbar.make(
                        requireView(),
                        "${result.exceptionOrNull()?.localizedMessage}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setLoginWithGoogleButton() {
        binding.signUpWithGoogleButton.setOnClickListener {
            signUpWithGoogle()
        }
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val credential = Identity.getSignInClient(requireActivity()).getSignInCredentialFromIntent(it.data)
            val idToken = credential.googleIdToken
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            viewmodel.onSignUpWithGoogle(firebaseCredential)
        }
    }

    private fun signUpWithGoogle(){
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
                    hideKeyboard()
                    Snackbar.make(
                        this.requireView(),
                        "\"Couldn't start One Tap UI: ${exception.localizedMessage}\"",
                        Snackbar.LENGTH_SHORT
                    ).show()
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

        binding.etConfirmPassword.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus){
                hideKeyboard()
            }
        }
    }
}

