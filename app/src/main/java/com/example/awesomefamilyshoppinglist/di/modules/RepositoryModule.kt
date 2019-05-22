package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.repositories.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        firebaseFirestore: FirebaseFirestore
    ): UserRepository =
        UserRepositoryImpl(firebaseAuth, firebaseFirestore)

    @Provides
    @Singleton
    open fun providesImageRepository(firebaseStorage: FirebaseStorage): ImageRepository =
        ImageRepositoryImpl(firebaseStorage)

    @Provides
    @Singleton
    open fun providesFamilyRepository(
        firebaseFirestore: FirebaseFirestore
    ): FamilyRepository =
        FamilyRepositoryImpl(firebaseFirestore)

}