package com.example.awesomefamilyshoppinglist.di.modules

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.example.awesomefamilyshoppinglist.main.MainContract
import com.example.awesomefamilyshoppinglist.main.MainRouterImpl
import com.example.awesomefamilyshoppinglist.main.MainViewModelImpl
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import com.example.awesomefamilyshoppinglist.splash.*
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderTestImpl
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import org.mockito.Mockito.mock
import javax.inject.Provider
import javax.inject.Singleton

/**
 * ALL FUNCTIONS MUST BE OPEN, SO MOCKITO CAN ACCESS THEM.
 * ALL DEPENDENCIES MUST BE DECLARED HERE, BECAUSE DAGGERMOCK ONLY
 * REPLACES INSTANCES FROM APPLICATION COMPONENT
 */
@Module
open class AppModuleForTest {

    @Provides
    @Singleton
    open fun providesSchedulerProvider(): SchedulerProvider = SchedulerProviderTestImpl()

    @Provides
    @Singleton
    open fun providesUserRepositoryImpl(): UserRepositoryImpl = UserRepositoryImpl()

    @Provides
    @Singleton
    open fun providesUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    open fun providesSplashViewModel(
        application: Application,
        userRepository: UserRepository,
        schedulerProvider: SchedulerProvider
    ): SplashViewModelImpl {
        return SplashViewModelImpl(application, userRepository, schedulerProvider)
    }

    @Provides
    open fun providesSplashViewModelFactory(provider: Provider<SplashViewModelImpl>) =
        SplashContract.ViewModelFactory(provider)

    @Provides
    @Singleton
    open fun providesSplashRouterImpl(
        userRepository: UserRepository
    ): SplashRouterImpl {
        return SplashRouterImpl(userRepository)
    }

    @Provides
    @Singleton
    open fun providesSplashRouter(
        impl: SplashRouterImpl
    ): SplashContract.Router {
        return impl
    }

    @Provides
    open fun providesMainViewModel(
        application: Application,
        repository: UserRepository,
        schedulerProvider: SchedulerProvider
    ): MainViewModelImpl {
        return MainViewModelImpl(application, repository, schedulerProvider)
    }

    @Provides
    open fun providesMainViewModelFactory(provider: Provider<MainViewModelImpl>) =
        MainContract.ViewModel.Companion.Factory(provider)

    @Provides
    @Singleton
    open fun providesMainRouterImpl(): MainContract.Router = MainRouterImpl()

}