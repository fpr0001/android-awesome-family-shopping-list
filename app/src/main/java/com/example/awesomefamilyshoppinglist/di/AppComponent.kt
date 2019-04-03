package com.example.awesomefamilyshoppinglist.di

import android.app.Application
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule
import com.example.awesomefamilyshoppinglist.di.modules.RepositoryModule
import com.example.awesomefamilyshoppinglist.di.modules.SplashModule
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
        SplashModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)
}
