package com.example.awesomefamilyshoppinglist.di.modules

import android.app.Application
import com.example.awesomefamilyshoppinglist.splash.SplashContract
import com.example.awesomefamilyshoppinglist.splash.SplashVMImpl
import com.example.awesomefamilyshoppinglist.splash.UserRepository
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

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