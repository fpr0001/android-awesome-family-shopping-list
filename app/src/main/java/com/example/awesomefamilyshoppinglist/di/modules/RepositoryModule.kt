package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Provides
    @Singleton
    open fun providesUserRepository(): UserRepository = UserRepositoryImpl()

}