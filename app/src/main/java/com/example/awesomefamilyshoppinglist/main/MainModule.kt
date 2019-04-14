package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
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
                repository: UserRepository,
                schedulerProvider: SchedulerProvider
            ): MainViewModelImpl {
                return MainViewModelImpl(application, repository, schedulerProvider)
            }

            @JvmStatic
            @Provides
            internal fun providesMainViewModelFactory(provider: Provider<MainViewModelImpl>) =
                MainContract.ViewModel.Companion.Factory(provider)

            @JvmStatic
            @Provides
            internal fun providesMainRouterImpl(activity: MainActivity): MainContract.Router = MainRouterImpl(activity)

        }
    }
}