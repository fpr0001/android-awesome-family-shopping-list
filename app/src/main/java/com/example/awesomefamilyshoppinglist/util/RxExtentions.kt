package com.example.awesomefamilyshoppinglist.util

import io.reactivex.CompletableEmitter
import io.reactivex.ObservableEmitter
import io.reactivex.SingleEmitter

fun <T> SingleEmitter<T>.safeOnError(exception: Throwable) {
    if (!isDisposed) {
        onError(exception)
    }
}

fun CompletableEmitter.safeOnError(exception: Throwable) {
    if (!isDisposed) {
        onError(exception)
    }
}

fun <T> ObservableEmitter<T>.safeOnError(exception: Throwable) {
    if (!isDisposed) {
        onError(exception)
    }
}