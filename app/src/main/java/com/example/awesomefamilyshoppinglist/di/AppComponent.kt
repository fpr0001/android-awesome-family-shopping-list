package com.example.awesomefamilyshoppinglist.di

import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule
import com.example.awesomefamilyshoppinglist.di.modules.RepositoryModule
import com.example.awesomefamilyshoppinglist.main.MainModule
import com.example.awesomefamilyshoppinglist.splash.SplashModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        RepositoryModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        SplashModule::class,
        MainModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)
}
