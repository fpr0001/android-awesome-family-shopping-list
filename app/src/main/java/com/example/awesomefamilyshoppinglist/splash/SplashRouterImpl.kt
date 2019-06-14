package com.example.awesomefamilyshoppinglist.splash

import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentActivity
import com.example.awesomefamilyshoppinglist.main.MainActivity
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.firebase.ui.auth.AuthUI

open class SplashRouterImpl(private val firebaseAuthUi: AuthUI) : SplashContract.Router {

    override fun goToMain(activity: FragmentActivity) {
        MainActivity.startActivity(activity)
    }

    override fun goToLogin(activity: FragmentActivity) {
        activity.startActivityForResult(getSignInIntent(), SplashContract.Router.CODE_SIGN_IN)
    }

    override fun getSignInIntent(): Intent {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        return firebaseAuthUi
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }
}