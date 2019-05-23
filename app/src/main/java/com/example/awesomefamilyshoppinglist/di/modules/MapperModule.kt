package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.repositories.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class MapperModule {

    @Provides
    open fun providesUserMapper() = UserMapper()

    @Provides
    open fun providesFamilyMapper() = FamilyMapper()

    @Provides
    open fun providesCategoryMapper() = CategoryMapper()

    @Provides
    open fun providesItemMapper() = ItemMapper()

}
