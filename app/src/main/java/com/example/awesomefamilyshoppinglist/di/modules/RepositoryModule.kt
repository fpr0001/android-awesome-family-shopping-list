package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Provides
    @Singleton
    open fun providesUserRepository(firebaseAuth: FirebaseAuth): UserRepository = UserRepositoryImpl(firebaseAuth)

}