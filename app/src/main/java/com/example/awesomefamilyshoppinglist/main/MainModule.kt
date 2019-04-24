package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
abstract class MainModule {

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
        internal fun providesMainRouterImpl(): MainContract.Router = MainRouterImpl()

    }
}