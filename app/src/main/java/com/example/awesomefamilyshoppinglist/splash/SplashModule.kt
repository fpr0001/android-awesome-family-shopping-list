package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Provider

@Module
abstract class SplashModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun providesSplashViewModel(
            application: Application,
            repository: UserRepository,
            schedulerProvider: SchedulerProvider
        ): SplashViewModelImpl {
            return SplashViewModelImpl(application, repository, schedulerProvider)
        }

        @JvmStatic
        @Provides
        internal fun providesSplashViewModelFactory(provider: Provider<SplashViewModelImpl>) =
            SplashContract.ViewModelFactory(provider)

        @JvmStatic
        @Provides
        internal fun providesSplashRouter(
            userRepository: UserRepository
        ): SplashContract.Router {
            return SplashRouterImpl(userRepository)
        }
    }
}