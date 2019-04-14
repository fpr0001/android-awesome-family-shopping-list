package com.example.awesomefamilyshoppinglist.splash

import androidx.fragment.app.FragmentActivity
import com.example.awesomefamilyshoppinglist.main.MainActivity
import com.example.awesomefamilyshoppinglist.repositories.UserRepository

internal class SplashRouterImpl(
    private val activity: FragmentActivity,
    private val userRepository: UserRepository
) : SplashContract.Router {

    override fun goToMain() {
        MainActivity.startActivity(activity)
    }

    override fun goToLogin() {
        activity.startActivityForResult(userRepository.getSignInIntent(), SplashContract.Router.CODE_SIGN_IN)
    }

}