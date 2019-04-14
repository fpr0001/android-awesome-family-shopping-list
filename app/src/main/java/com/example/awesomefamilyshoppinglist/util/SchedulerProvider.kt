package com.example.awesomefamilyshoppinglist.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Provider

interface SchedulerProvider {
    fun <T> async(single: Single<T>): Single<T>
    fun <T> async(observable: Observable<T>): Observable<T>
    fun async(completable: Completable): Completable
}

abstract class BaseSchedulerImpl(
    protected val providerIo: Provider<Scheduler>,
    protected val providerMain: Provider<Scheduler>
) : SchedulerProvider {

    override fun async(completable: Completable): Completable {
        return completable
            .subscribeOn(providerIo.get())
            .observeOn(providerMain.get())
    }

    override fun <T> async(observable: Observable<T>): Observable<T> {
        return observable
            .subscribeOn(providerIo.get())
            .observeOn(providerMain.get())
    }

    override fun <T> async(single: Single<T>): Single<T> {
        return single
            .subscribeOn(providerIo.get())
            .observeOn(providerMain.get())
    }
}

open class SchedulerProviderImpl : BaseSchedulerImpl(
    providerIo = Provider { Schedulers.io() },
    providerMain = Provider { AndroidSchedulers.mainThread() })

open class SchedulerProviderTestImpl : BaseSchedulerImpl(
    providerIo = Provider { Schedulers.trampoline() },
    providerMain = Provider { AndroidSchedulers.mainThread() })