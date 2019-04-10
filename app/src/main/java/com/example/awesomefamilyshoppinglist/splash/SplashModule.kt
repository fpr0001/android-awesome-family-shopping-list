package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Provider

@Module
abstract class SplashModule {

    @ContributesAndroidInjector(modules = [SplashVmModule::class])
    abstract fun bind(): SplashActivity

    @Module
    class SplashVmModule {

        @Module
        companion object {

            @JvmStatic
            @Provides
            internal fun providesSplashViewModel(
                application: Application,
                repository: UserRepository
            ): SplashContract.ViewModelImpl {
                return SplashContract.ViewModelImpl(application, repository)
            }

            @JvmStatic
            @Provides
            internal fun providesSplashViewModelFactory(provider: Provider<SplashContract.ViewModelImpl>) =
                SplashContract.ViewModel.Companion.Factory(provider)
        }

    }
}