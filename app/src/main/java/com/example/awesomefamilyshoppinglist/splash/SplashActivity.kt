package com.example.awesomefamilyshoppinglist.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.awesomefamilyshoppinglist.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : FragmentActivity() {

    companion object {
        const val RC_SIGN_IN = 1
    }

    @Inject
    internal lateinit var vmFactory: SplashContract.ViewModel.Companion.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val vm = vmFactory.getViewModel(this)

        setContentView(R.layout.activity_splash)

        button.setOnClickListener {
//            vmSplash.printAddresses()
//            println(vmSplash)
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