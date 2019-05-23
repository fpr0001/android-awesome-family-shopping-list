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
        firebaseFirestore: FirebaseFirestore,
        userMapper: UserMapper
    ): UserRepository =
        UserRepositoryImpl(firebaseAuth, firebaseFirestore, userMapper)

    @Provides
    @Singleton
    open fun providesImageRepository(firebaseStorage: FirebaseStorage): ImageRepository =
        ImageRepositoryImpl(firebaseStorage)

    @Provides
    @Singleton
    open fun providesFamilyRepository(
        familyMapper: FamilyMapper
    ): FamilyRepository =
        FamilyRepositoryImpl(familyMapper)

    @Provides
    @Singleton
    open fun providesItemsRepository(
        firebaseFirestore: FirebaseFirestore,
        itemMapper: ItemMapper
    ): ItemsRepository =
        ItemsRepositoryImpl(firebaseFirestore, itemMapper)

    @Provides
    @Singleton
    open fun providesCategoryRepository(
        firebaseFirestore: FirebaseFirestore,
        categoryMapper: CategoryMapper
    ): CategoryRepository =
        CategoryRepositoryImpl(firebaseFirestore, categoryMapper)

}