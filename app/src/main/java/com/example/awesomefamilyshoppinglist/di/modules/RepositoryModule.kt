package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.splash.UserRepository
import com.example.awesomefamilyshoppinglist.splash.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

}