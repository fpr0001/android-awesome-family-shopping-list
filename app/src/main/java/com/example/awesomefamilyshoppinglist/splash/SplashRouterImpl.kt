package com.example.awesomefamilyshoppinglist.splash

import androidx.fragment.app.FragmentActivity
import com.example.awesomefamilyshoppinglist.main.MainActivity
import com.example.awesomefamilyshoppinglist.repositories.UserRepository

open class SplashRouterImpl(
    private val userRepository: UserRepository
) : SplashContract.Router {

    override fun goToMain(activity: FragmentActivity) {
        MainActivity.startActivity(activity)
    }

    override fun goToLogin(activity: FragmentActivity) {
        activity.startActivityForResult(userRepository.getSignInIntent(), SplashContract.Router.CODE_SIGN_IN)
    }

}