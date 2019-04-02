package com.example.awesomefamilyshoppinglist.di.modules

import android.app.Application
import com.example.awesomefamilyshoppinglist.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Singleton
    @Provides
    fun providesApplication() = application

}