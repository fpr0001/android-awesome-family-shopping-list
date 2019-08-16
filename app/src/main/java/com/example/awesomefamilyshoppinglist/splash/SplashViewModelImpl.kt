package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import io.reactivex.rxkotlin.addTo

open class SplashViewModelImpl(
    application: Application,
    private val useCases: SplashContract.UseCases,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(application), SplashContract.ViewModel {

    override val statusLiveData: MutableLiveData<SplashContract.Status> =
        MutableLiveData(SplashContract.Status.StatusLoggedOut)
    override val tryAgainVisibility = ObservableInt(View.GONE)

    override fun autoLogin() {
        showProgressBar()
        tryAgainVisibility.set(View.GONE)
        schedulerProvider
            .async { observeOn -> useCases.fetchAndStoreEntities(observeOn) }
            .subscribe({
                hideProgressBar()
                statusLiveData.value = SplashContract.Status.StatusLoggedIn
            }, { throwable ->
                hideProgressBar()
                statusLiveData.value =
                    (throwable as? SplashContract.Exception)?.status ?: SplashContract.Status.StatusUnexpectedError
            })
            .addTo(compositeDisposable)
    }

    override fun enableTryAgain() {
        tryAgainVisibility.set(View.VISIBLE)
    }
}