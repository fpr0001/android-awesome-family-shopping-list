package com.example.awesomefamilyshoppinglist.di.modules

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.awesomefamilyshoppinglist.main.*
import com.example.awesomefamilyshoppinglist.repositories.*
import com.example.awesomefamilyshoppinglist.splash.*
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderTestImpl
import com.example.awesomefamilyshoppinglist.utils.app
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.spy
import javax.inject.Provider
import javax.inject.Singleton

/**
 * ALL FUNCTIONS MUST BE OPEN, SO MOCKITO CAN ACCESS THEM.
 * ALL DEPENDENCIES MUST BE DECLARED HERE, BECAUSE DAGGERMOCK ONLY
 * REPLACES INSTANCES FROM APPLICATION COMPONENT
 */
@Module
open class AppModuleForTest {

    // region ******************* Firebase **********************

    @Provides
    @Singleton
    open fun providesFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    open fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    open fun providesFirebaseAuthUi(): AuthUI = AuthUI.getInstance()

    @Provides
    @Singleton
    open fun providesFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    //endregion

    @Provides
    @Singleton
    open fun providesSchedulerProvider(): SchedulerProvider = SchedulerProviderTestImpl()

    // region ******************* Repositories **********************

    @Provides
    @Singleton
    open fun providesUserRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userMapper: UserMapper
    ): UserRepositoryImpl =
        UserRepositoryImpl(firebaseAuth, firestore, userMapper)

    @Provides
    @Singleton
    open fun providesFamilyRepositoryImpl(
        familyMapper: FamilyMapper
    ): FamilyRepositoryImpl =
        FamilyRepositoryImpl(familyMapper)

    @Provides
    @Singleton
    open fun providesCategoryRepositoryImpl(
        firestore: FirebaseFirestore,
        categoryMapper: CategoryMapper
    ): CategoryRepositoryImpl =
        CategoryRepositoryImpl(firestore, categoryMapper)

    @Provides
    @Singleton
    open fun providesItemsRepositoryImpl(
        firestore: FirebaseFirestore,
        itemMapper: ItemMapper
    ): ItemsRepositoryImpl =
        ItemsRepositoryImpl(firestore, itemMapper)

    @Provides
    @Singleton
    open fun providesUserRepository(impl: UserRepositoryImpl): UserRepository = impl


    @Provides
    @Singleton
    open fun providesFamilyRepository(impl: FamilyRepositoryImpl): FamilyRepository = impl


    @Provides
    @Singleton
    open fun providesCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository = impl


    @Provides
    @Singleton
    open fun providesItemsRepository(impl: ItemsRepositoryImpl): ItemsRepository = impl
    //endregion

    // region ******************* Mappers **********************

    @Provides
    open fun providesUserMapper(): UserMapper = UserMapper()

    @Provides
    open fun providesFamilyMapper(): FamilyMapper = FamilyMapper()

    @Provides
    open fun providesCategoryMapper(): CategoryMapper = CategoryMapper()

    @Provides
    open fun providesItemMapper(): ItemMapper = ItemMapper()

    //endregion

    // region ******************* UseCases **********************

    @Provides
    open fun providesSplashUseCasesImpl(
        userRepository: UserRepository,
        familyRepository: FamilyRepository,
        categoryRepository: CategoryRepository
    ): SplashUseCasesImpl {
        return SplashUseCasesImpl(userRepository, familyRepository, categoryRepository)
    }

    @Provides
    open fun providesSplashUseCase(splashUseCasesImpl: SplashUseCasesImpl): SplashContract.UseCases {
        return splashUseCasesImpl
    }

    @Provides
    open fun providesMainUseCasesImpl(
        userRepository: UserRepository,
        familyRepository: FamilyRepository,
        categoryRepository: CategoryRepository,
        itemsRepository: ItemsRepository
    ): MainUseCasesImpl {
        return MainUseCasesImpl(userRepository, familyRepository, categoryRepository, itemsRepository)
    }

    @Provides
    open fun providesMainUseCase(useCasesImpl: MainUseCasesImpl): MainContract.UseCases {
        return useCasesImpl
    }


    //endregion

    // region ******************* ViewModels **********************

    @Provides
    open fun providesSplashViewModel(
        application: Application,
        useCases: SplashContract.UseCases,
        schedulerProvider: SchedulerProvider
    ): SplashViewModelImpl {
        return SplashViewModelImpl(application, useCases, schedulerProvider)
    }

    @Provides
    open fun providesMainViewModelImpl(
        useCases: MainContract.UseCases,
        schedulerProvider: SchedulerProvider
    ): MainViewModelImpl {
        return MainViewModelImpl(
            app,
            useCases,
            schedulerProvider
        )
    }

    @Provides
    open fun providesSplashViewModelFactory(provider: Provider<SplashViewModelImpl>) =
        SplashContract.ViewModelFactory(provider)

    @Provides
    @Singleton
    open fun providesMainViewModel(vmi: MainViewModelImpl): MainContract.ViewModel = spy(vmi)

//endregion

// region ******************* Routers **********************

    @Provides
    @Singleton
    open fun providesSplashRouterImpl(
        firebaseAuthUi: AuthUI
    ): SplashRouterImpl {
        return SplashRouterImpl(firebaseAuthUi)
    }

    @Provides
    @Singleton
    open fun providesSplashRouter(
        impl: SplashRouterImpl
    ): SplashContract.Router {
        return impl
    }

    @Provides
    @Singleton
    open fun providesMainRouterImpl(): MainContract.Router = MainRouterImpl()

//endregion

//region ******************* Adapters ********************

    @Provides
    internal fun providesMainAdapter(): MainAdapter = MainAdapter()

//endregion
}