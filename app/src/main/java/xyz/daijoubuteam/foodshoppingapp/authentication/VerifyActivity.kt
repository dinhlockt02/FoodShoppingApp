package xyz.daijoubuteam.foodshoppingapp.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.MainActivity
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.ActivityVerifyBinding

class VerifyActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var binding: ActivityVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify)
        addAuthStateListener()
        setLogoutButton()
        setResendButton()
        setUserEmail()
        setCheckUserVerifyStatus()
    }

    private fun setUserEmail(){
        binding.emailTextView.text = "We have just sent an email to ${auth.currentUser!!.email} for authentication"
    }

    private fun addAuthStateListener() {
        auth.addAuthStateListener {
            if(it.currentUser == null) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else if(it.currentUser!!.isEmailVerified){
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun setLogoutButton(){
        binding.btnLogout.setOnClickListener {
            auth.signOut()
        }
    }

    private fun setResendButton(){
        binding.btnResend.setOnClickListener {
            auth.currentUser!!.sendEmailVerification()
            Snackbar.make(binding.coordinatorLayout, "Resend successful", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setCheckUserVerifyStatus(){
        lifecycleScope.launch {
            while(true){
                delay(1_000)
                auth.currentUser?.reload()?.await()
                if(auth.currentUser!!.isEmailVerified){
                    onAuthStateChanged()
                }
            }
        }
    }

    private fun onAuthStateChanged(){
        if(auth.currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        else if(auth.currentUser!!.isEmailVerified){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}