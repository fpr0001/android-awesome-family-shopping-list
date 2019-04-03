package com.example.awesomefamilyshoppinglist

import android.app.Activity
import android.app.Application
import com.example.awesomefamilyshoppinglist.di.AppComponent
import com.example.awesomefamilyshoppinglist.di.DaggerAppComponent
import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
            .inject(this)

//        appComponent = DaggerAppComponent
//            .builder()
//            .applicationModule(ApplicationModule(this))
//            .build()

    }

}