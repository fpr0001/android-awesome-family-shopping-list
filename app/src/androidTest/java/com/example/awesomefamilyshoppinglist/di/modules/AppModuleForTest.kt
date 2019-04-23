package com.example.awesomefamilyshoppinglist.di.modules

import android.app.Application
import androidx.fragment.app.FragmentActivity
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
    open fun providesSplashRouter(
        userRepository: UserRepository
    ): SplashContract.Router {
        return SplashRouterImpl(userRepository)
    }
}