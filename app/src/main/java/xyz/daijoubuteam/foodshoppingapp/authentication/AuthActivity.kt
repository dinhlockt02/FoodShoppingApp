package xyz.daijoubuteam.foodshoppingapp.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import xyz.daijoubuteam.foodshoppingapp.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

}