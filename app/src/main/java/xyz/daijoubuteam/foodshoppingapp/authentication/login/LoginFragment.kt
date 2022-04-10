package xyz.daijoubuteam.foodshoppingapp.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var callbackManager: CallbackManager
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

        initialFacebookLoginButton()

        setNavigateToSignUpObserver()
        setLoginResultObserver()
        setLoginWithGoogleButton()

        autoLogin()

        return binding.root
    }

    private fun setLoginWithGoogleButton() {
        binding.linearLoginWithGoogle.setOnClickListener {
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
                    loginSuccessful()
                } else if (result.isFailure) {
                    Toast.makeText(
                        this.requireContext(),
                        "${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    viewmodel.onLoginComplete()
                }
            }
        }
    }

    private fun initialFacebookLoginButton(){
        callbackManager = CallbackManager.Factory.create()
        binding.facebookLoginButton.setPermissions("email", "public_profile")
        binding.facebookLoginButton.fragment = this
        binding.facebookLoginButton.registerCallback(callbackManager, object:
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                viewmodel.onLoginWithFacebook(result.accessToken)
            }

            override fun onCancel() {
                Log.d("login", "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d("login", "facebook:onError", error)
            }

        })
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
                    Log.e("login", "Couldn't start One Tap UI: ${exception.localizedMessage}")
                }
            }
            .addOnFailureListener {
                Log.e("login", "Couldn't start One Tap UI: ${it.localizedMessage}")
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