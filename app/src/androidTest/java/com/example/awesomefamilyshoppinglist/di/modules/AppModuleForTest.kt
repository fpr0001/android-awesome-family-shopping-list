package com.example.awesomefamilyshoppinglist.di.modules

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import com.example.awesomefamilyshoppinglist.splash.SplashActivity
import com.example.awesomefamilyshoppinglist.splash.SplashContract
import com.example.awesomefamilyshoppinglist.splash.SplashRouterImpl
import com.example.awesomefamilyshoppinglist.splash.SplashViewModelImpl
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderTestImpl
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Provider
import javax.inject.Singleton

@Module
open class AppModuleForTest {

    @Provides
    @Singleton
    fun providesSchedulerProvider(): SchedulerProvider = SchedulerProviderTestImpl()

    @Provides
    @Singleton
    open fun providesUserRepository(): UserRepository = UserRepositoryImpl()

    @Provides
    internal fun providesSplashViewModel(
        application: Application,
        userRepository: UserRepository,
        schedulerProvider: SchedulerProvider
    ): SplashViewModelImpl {
        return SplashViewModelImpl(application, userRepository, schedulerProvider)
    }

    @Provides
    internal fun providesSplashViewModelFactory(provider: Provider<SplashViewModelImpl>) =
        SplashContract.ViewModelFactory(provider)
}