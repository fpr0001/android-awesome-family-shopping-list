package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner
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
        internal fun providesMainViewModelImpl(
            application: Application,
            useCases: MainContract.UseCases,
            schedulerProvider: SchedulerProvider
        ): MainViewModelImpl {
            return MainViewModelImpl(
                application,
                useCases,
                schedulerProvider
            )
        }

        @JvmStatic
        @Provides
        internal fun providesMainUseCases(
            userRepository: UserRepository,
            familyRepository: FamilyRepository,
            categoryRepository: CategoryRepository,
            itemsRepository: ItemsRepository
        ): MainContract.UseCases {
            return MainUseCasesImpl(userRepository, familyRepository, categoryRepository, itemsRepository)
        }

        @JvmStatic
        @Provides
        internal fun providesMainViewModelFactory(provider: Provider<MainViewModelImpl>) =
            MainContract.ViewModel.Companion.Factory(provider)

        @JvmStatic
        @Provides
        internal fun providesStoreOwner(activity: MainActivity): ViewModelStoreOwner = activity

        @JvmStatic
        @Provides
        internal fun providesMainViewModel(
            storeOwner: ViewModelStoreOwner,
            factory: MainContract.ViewModel.Companion.Factory
        ): MainContract.ViewModel =
            factory.getViewModel(storeOwner)

        @JvmStatic
        @Provides
        internal fun providesMainRouterImpl(): MainContract.Router = MainRouterImpl()

        @JvmStatic
        @Provides
        internal fun providesMainAdapter(): MainAdapter = MainAdapter()
    }
}