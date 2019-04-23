package com.example.awesomefamilyshoppinglist.di.modules

import androidx.fragment.app.FragmentActivity
import com.example.awesomefamilyshoppinglist.main.MainActivity
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.splash.*
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import org.mockito.Mockito
import org.mockito.Mockito.spy
import javax.inject.Singleton

@Module
abstract class ActivitiesBindingModuleForTest {

    @ContributesAndroidInjector
    abstract fun bindSplash(): SplashActivity

//    @ContributesAndroidInjector
//    abstract fun bindMain(): MainActivity
}