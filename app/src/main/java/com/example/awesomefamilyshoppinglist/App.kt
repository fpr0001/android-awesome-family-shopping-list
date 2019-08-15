package com.example.awesomefamilyshoppinglist

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.ndk.CrashlyticsNdk
import com.example.awesomefamilyshoppinglist.di.DaggerAppComponent
import com.example.awesomefamilyshoppinglist.util.CrashReportingTree
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAnroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAnroidInjector

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
            .provideApplication(this)
            .build()
            .inject(this)

    }

}