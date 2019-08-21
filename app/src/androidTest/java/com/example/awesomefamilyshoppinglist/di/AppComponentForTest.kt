package com.example.awesomefamilyshoppinglist.di

import android.app.Application
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.di.modules.ActivitiesBindingModuleForTest
import com.example.awesomefamilyshoppinglist.di.modules.AppModuleForTest
import com.example.awesomefamilyshoppinglist.main.MainActivityTest
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.splash.SplashContract
import com.example.awesomefamilyshoppinglist.utils.AppForTests
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivitiesBindingModuleForTest::class,
        AppModuleForTest::class]
)
interface AppComponentForTest : AndroidInjector<AppForTests> {

    @Component.Builder
    interface Builder {

        fun appModuleForTest(appModuleForTest: AppModuleForTest): Builder

        @BindsInstance
        fun provideApplication(app: Application): Builder

        fun build(): AppComponentForTest
    }

    fun getUserRepository(): UserRepository

    fun getSplashRouter(): SplashContract.Router

    fun inject(test: MainActivityTest)
}