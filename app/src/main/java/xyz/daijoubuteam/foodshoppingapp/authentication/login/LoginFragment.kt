package xyz.daijoubuteam.foodshoppingapp.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.authentication.AuthActivity
import xyz.daijoubuteam.foodshoppingapp.authentication.signup.SignUpFragmentDirections
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest


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

        initOneTapSignIn()

        setNavigateToSignUpObserver()
        setLoginResultObserver()
        setLoginWithGoogleEventObserver()

        autoLogin()

        return binding.root
    }

    private fun setNavigateToSignUpObserver() {
        viewmodel.navigateToSignUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                viewmodel.onNavigateToSignupComplete()
            }
        })
    }

    private fun setLoginResultObserver() {
        viewmodel.loginResult.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                if (result.isSuccess) {
                    viewmodel.onLoginWithEmailAndPasswordComplete()
                    loginSuccessful()
                } else if (result.isFailure) {
                    Toast.makeText(
                        this.requireContext(),
                        "${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    viewmodel.onLoginWithEmailAndPasswordComplete()
                }
            }
        })
    }

    private fun setLoginWithGoogleEventObserver(){
        viewmodel.loginWithGoogleEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(this.requireActivity()) {result ->
                        try {
                            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            activityResultLauncher.launch(intentSenderRequest)
                        } catch (exception: Exception){
                            Log.e("login", "Couldn't start One Tap UI: ${exception.localizedMessage}")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("login", "Couldn't start One Tap UI: ${it.localizedMessage}")
                    }
            }
        })
    }

    private fun initOneTapSignIn(){
        oneTapClient = Identity.getSignInClient(this.requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build())
            .build()

    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            val credential = oneTapClient.getSignInCredentialFromIntent(it.data)
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