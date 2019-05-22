package com.example.awesomefamilyshoppinglist.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider

interface SchedulerProvider {
    fun <T> async(single: Single<T>): Single<T>
    fun <T> async(observable: Observable<T>): Observable<T>
    fun async(completable: Completable): Completable
    fun <T> async(single: (observeOn: Scheduler) -> Single<T>): Single<T>
}

abstract class BaseSchedulerImpl : SchedulerProvider {

    private lateinit var providerIo: Provider<Scheduler>
    private lateinit var providerMain: Provider<Scheduler>

    protected constructor()

    constructor(
        providerIo: Provider<Scheduler>,
        providerMain: Provider<Scheduler>
    ) : this() {
        this.providerIo = providerIo
        this.providerMain = providerMain
    }

    constructor(provider: Provider<Scheduler>) : this() {
        this.providerMain = provider
        this.providerIo = provider
    }

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

    override fun <T> async(single: (observeOn: Scheduler) -> Single<T>): Single<T> {
        return single(providerIo.get())
            .subscribeOn(providerIo.get())
            .observeOn(providerMain.get())
    }
}

open class SchedulerProviderImpl @Inject constructor() : BaseSchedulerImpl(
    providerIo = Provider { Schedulers.io() },
    providerMain = Provider { AndroidSchedulers.mainThread() })

open class SchedulerProviderTestImpl : BaseSchedulerImpl(Provider { Schedulers.trampoline() })