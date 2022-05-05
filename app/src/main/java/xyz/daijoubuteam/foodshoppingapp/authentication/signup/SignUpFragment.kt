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
        setupMessageObserver()

        return binding.root
    }

    private fun setupMessageObserver(){
        viewmodel.message.observe(viewLifecycleOwner){
            if(!it.isNullOrBlank()){
                hideKeyboard()
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                viewmodel.onShowMessageComplete()
            }
        }
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
                    viewmodel.onShowMessage("Đăng ký thành công.")
                } else if (result.isFailure) {
                    viewmodel.onShowMessage("${result.exceptionOrNull()?.localizedMessage}")
                }
                viewmodel.onSignUpComplete()
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
                    viewmodel.onShowMessage("\"Couldn't start One Tap UI: ${exception.localizedMessage}\"")
                }
            }
            .addOnFailureListener {
                viewmodel.onShowMessage("\"Couldn't start One Tap UI: ${it.localizedMessage}\"")
            }
    }

    private fun setupSoftKeyboardUI(){
        binding.etEmail.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowSoftKeyboard()){
                hideKeyboard()
            }
        }
        binding.etPassword.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowSoftKeyboard()){
                hideKeyboard()
            }
        }

        binding.etConfirmPassword.setOnFocusChangeListener { view, hasFocus ->
            if(!shouldShowSoftKeyboard()){
                hideKeyboard()
            }
        }
    }

    private fun shouldShowSoftKeyboard(): Boolean{
        return binding.etEmail.hasFocus() || binding.etPassword.hasFocus() || binding.etConfirmPassword.hasFocus()
    }
}

