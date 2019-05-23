package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.google.firebase.firestore.FirebaseFirestoreException
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.zipWith
import timber.log.Timber
import java.lang.RuntimeException

open class SplashViewModelImpl(
    application: Application,
    private val splashUseCases: SplashContract.SplashUseCases,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(application), SplashContract.ViewModel {

    override val statusLiveData: MutableLiveData<SplashContract.Status> =
        MutableLiveData(SplashContract.Status.StatusLoggedOut)
    override val tryAgainVisibility = ObservableInt(View.GONE)

    override fun autoLogin() {
        showProgressBar()
        tryAgainVisibility.set(View.GONE)
        schedulerProvider
            .async { observeOn -> splashUseCases.fetchAndStoreEntities(observeOn) }
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