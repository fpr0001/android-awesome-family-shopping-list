package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.main.MainActivity
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.splash.*
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBindingModuleForTest {

    @ContributesAndroidInjector
    abstract fun bindSplash(): SplashActivity

//    @ContributesAndroidInjector
//    abstract fun bindMain(): MainActivity
}

