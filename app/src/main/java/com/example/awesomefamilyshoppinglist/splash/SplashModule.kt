package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderImpl
import com.firebase.ui.auth.AuthUI
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
            splashUseCases: SplashContract.SplashUseCases,
            schedulerProvider: SchedulerProvider
        ): SplashViewModelImpl {
            return SplashViewModelImpl(application, splashUseCases, schedulerProvider)
        }

        @JvmStatic
        @Provides
        internal fun providesSplashViewModelFactory(provider: Provider<SplashViewModelImpl>) =
            SplashContract.ViewModelFactory(provider)

        @JvmStatic
        @Provides
        internal fun providesSplashRouter(
            firebaseAuthUi: AuthUI
        ): SplashContract.Router {
            return SplashRouterImpl(firebaseAuthUi)
        }

        @JvmStatic
        @Provides
        internal fun providesSplashUseCases(
            userRepository: UserRepository,
            familyRepository: FamilyRepository,
            categoryRepository: CategoryRepository
        ): SplashContract.SplashUseCases {
            return SplashUseCasesImpl(userRepository, familyRepository, categoryRepository)
        }
    }
}