package com.example.awesomefamilyshoppinglist.di

import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule
import com.example.awesomefamilyshoppinglist.di.modules.RepositoryModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        RepositoryModule::class,
        AndroidInjectionModule::class
    ]
)
interface AppComponent {

    fun splashSubcomponent(): SplashSubcomponent

}
