package com.example.awesomefamilyshoppinglist

import android.app.Activity
import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.ndk.CrashlyticsNdk
import com.example.awesomefamilyshoppinglist.di.DaggerAppComponent
import com.example.awesomefamilyshoppinglist.di.modules.ApplicationModule
import com.example.awesomefamilyshoppinglist.util.CrashReportingTree
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject
import timber.log.Timber
import timber.log.Timber.DebugTree




class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics(), CrashlyticsNdk())

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        DaggerAppComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
            .inject(this)

    }

}