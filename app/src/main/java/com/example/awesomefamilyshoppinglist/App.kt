package com.example.awesomefamilyshoppinglist

import android.app.Application
import com.example.awesomefamilyshoppinglist.di.AppComponent
import com.example.awesomefamilyshoppinglist.di.DaggerAppComponent
import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()

    }

}