package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.di.SplashSubcomponent
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SplashSubcomponent::class])
abstract class SplashModule {

    @Binds
    @IntoMap
    @ClassKey(SplashActivity::class)
    abstract fun bindSplashActivityInjectorFactory(builder: SplashSubcomponent.Builder): AndroidInjector.Factory<*>

}