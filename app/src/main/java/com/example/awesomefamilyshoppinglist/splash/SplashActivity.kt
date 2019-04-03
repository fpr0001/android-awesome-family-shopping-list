package com.example.awesomefamilyshoppinglist.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.awesomefamilyshoppinglist.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import dagger.android.DaggerActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : DaggerActivity() {

    companion object {
        const val RC_SIGN_IN = 1
    }

    @Inject
    lateinit var vmSplash: SplashContract.SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        vm = ViewModelProviders.of(this, vmSplashFactory).get(SplashVMImpl::class.java)

        setContentView(R.layout.activity_splash)

        button.setOnClickListener {
            vmSplash.printAddresses()
            println(vmSplash)
        }
        button2.setOnClickListener {
            AndroidInjection.inject(this)
        }

    }

    private fun loginIfNeeded() {
        if (FirebaseAuth.getInstance().currentUser == null) {

            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}