package com.example.awesomefamilyshoppinglist.di

import com.example.awesomefamilyshoppinglist.di.modules.SplashModule
import com.example.awesomefamilyshoppinglist.di.scopes.ActivityScope
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SplashModule::class])
interface SplashSubcomponent {

    fun inject(activity: SplashActivity)

}