package com.example.awesomefamilyshoppinglist.di

import android.app.Application
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.di.modules.ActivitiesBindingModule
import com.example.awesomefamilyshoppinglist.di.modules.RepositoryModule
import com.example.awesomefamilyshoppinglist.di.modules.UtilsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        UtilsModule::class,
        AndroidInjectionModule::class,
        ActivitiesBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideApplication(app: Application): Builder

        fun build(): AppComponent
    }
}