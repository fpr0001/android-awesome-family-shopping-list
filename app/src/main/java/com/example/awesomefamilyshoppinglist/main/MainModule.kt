package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.ItemsRepository
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
            userRepository: UserRepository,
            familyRepository: FamilyRepository,
            categoryRepository: CategoryRepository,
            itemsRepository: ItemsRepository,
            schedulerProvider: SchedulerProvider
        ): MainViewModelImpl {
            return MainViewModelImpl(
                application,
                userRepository,
                familyRepository,
                categoryRepository,
                itemsRepository,
                schedulerProvider
            )
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