package com.example.awesomefamilyshoppinglist.main

import android.content.Context
import com.example.awesomefamilyshoppinglist.splash.SplashActivity

open class MainRouterImpl : MainContract.Router {

    override fun goToSplash(context: Context) {
        SplashActivity.startActivity(context)
    }

    override fun goToHistory(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}