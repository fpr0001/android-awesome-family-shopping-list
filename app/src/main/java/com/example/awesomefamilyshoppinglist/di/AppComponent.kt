package com.example.awesomefamilyshoppinglist.di

import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule
import com.example.awesomefamilyshoppinglist.di.modules.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class])
interface AppComponent {

    fun splashSubcomponent(): SplashSubcomponent

}
