package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Provider

@Module
abstract class MainModule {

    @ContributesAndroidInjector(modules = [MainVmModule::class])
    abstract fun bind(): MainActivity

    @Module
    class MainVmModule {

        @Module
        companion object {

            @JvmStatic
            @Provides
            internal fun providesMainViewModel(
                application: Application,
                repository: UserRepository
            ): MainContract.ViewModelImpl {
                return MainContract.ViewModelImpl(application, repository)
            }

            @JvmStatic
            @Provides
            internal fun providesSplashViewModelFactory(provider: Provider<MainContract.ViewModelImpl>) =
                MainContract.ViewModel.Companion.Factory(provider)
        }

    }
}