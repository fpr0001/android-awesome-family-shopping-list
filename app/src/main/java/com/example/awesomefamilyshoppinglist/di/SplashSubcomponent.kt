package com.example.awesomefamilyshoppinglist.di

import android.app.Application
import com.example.awesomefamilyshoppinglist.di.scopes.ActivityScope
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import com.example.awesomefamilyshoppinglist.splash.SplashContract
import com.example.awesomefamilyshoppinglist.splash.SplashVMImpl
import com.example.awesomefamilyshoppinglist.splash.UserRepository
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [SplashVmModule::class])
interface SplashSubcomponent : AndroidInjector<SplashActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SplashActivity>()
}

@Module
class SplashVmModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun providesSplashViewModel(
            application: Application,
            repository: UserRepository
        ): SplashContract.SplashViewModel {
            return SplashVMImpl(application, repository)
        }
    }
}

