package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.repositories.ImageRepository
import com.example.awesomefamilyshoppinglist.repositories.ImageRepositoryImpl
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Provides
    @Singleton
    open fun providesUserRepository(
        firebaseAuth: FirebaseAuth,
        imageRepository: ImageRepository
    ): UserRepository =
        UserRepositoryImpl(firebaseAuth, imageRepository)

    @Provides
    @Singleton
    open fun providesImageRepository(firebaseStorage: FirebaseStorage): ImageRepository =
        ImageRepositoryImpl(firebaseStorage)

}