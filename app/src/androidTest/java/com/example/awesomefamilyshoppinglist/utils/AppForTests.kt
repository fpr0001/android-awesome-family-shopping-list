package com.example.awesomefamilyshoppinglist.utils

import android.app.Application
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.di.AppComponentForTest
import com.example.awesomefamilyshoppinglist.di.DaggerAppComponentForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import com.example.awesomefamilyshoppinglist.util.CrashReportingTree
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class AppForTests : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAnroidInjector: DispatchingAndroidInjector<Any>

    lateinit var component: AppComponentForTest

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAnroidInjector

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        DaggerAppComponentForTest
            .builder()
            .provideApplication(this)
            .appModuleForTest(AppModuleForTest())
            .build()
            .also { component = it }
            .inject(this)
    }
}