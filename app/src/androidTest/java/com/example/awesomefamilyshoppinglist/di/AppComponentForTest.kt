package com.example.awesomefamilyshoppinglist.di

import android.app.Application
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.di.modules.ActivitiesBindingModuleForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModuleForTest::class,
        ActivitiesBindingModuleForTest::class
    ]
)
interface AppComponentForTest: AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        fun appModuleForTest(appModuleForTest: AppModuleForTest):Builder

        @BindsInstance
        fun provideApplication(app: Application):Builder

        fun build(): AppComponentForTest
    }
}