package com.example.awesomefamilyshoppinglist.main

import android.content.Context
import com.example.awesomefamilyshoppinglist.splash.SplashActivity

internal class MainRouterImpl(private val context: Context) : MainContract.Router {

    override fun goToSplash() {
        SplashActivity.startActivity(context)
    }

    override fun goToHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}