package com.example.awesomefamilyshoppinglist.di.modules

import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class UtilsModule {

    @Singleton
    @Binds
    abstract fun bindsSchedulerProvider(schedulerProviderImpl: SchedulerProviderImpl): SchedulerProvider

}