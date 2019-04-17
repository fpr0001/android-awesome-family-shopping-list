package com.example.awesomefamilyshoppinglist.splash

import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import dagger.Module
import dagger.Provides

@Module
class SplashRouterModule {

    @Provides
    internal fun providesSplashRouter(
        activity: SplashActivity,
        userRepository: UserRepository
    ): SplashContract.Router {
        return SplashRouterImpl(activity, userRepository)
    }
}