package com.example.awesomefamilyshoppinglist.util

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), BaseViewModelI {

    protected val compositeDisposable = CompositeDisposable()
    override val progressBarVisibility = ObservableInt(View.GONE)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun showProgressBar() = progressBarVisibility.set(View.VISIBLE)
    protected fun hideProgressBar() = progressBarVisibility.set(View.GONE)
}

interface BaseViewModelI {
    val progressBarVisibility: ObservableInt
}