package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.main.MainActivity
import com.example.awesomefamilyshoppinglist.main.MainModule
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import com.example.awesomefamilyshoppinglist.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBindingModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMain(): MainActivity

    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun bindSplash(): SplashActivity
}