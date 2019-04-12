package com.example.awesomefamilyshoppinglist.util

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.async(): Single<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.async(): Observable<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.inIo(): Single<T> = subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

fun Completable.inIo(): Completable = subscribeOn(Schedulers.io()).observeOn(Schedulers.io())

fun Completable.async(): Completable = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())