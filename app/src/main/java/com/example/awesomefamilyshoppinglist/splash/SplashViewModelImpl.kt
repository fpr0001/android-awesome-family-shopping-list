package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

open class SplashViewModelImpl(
    application: Application,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
    ) : BaseViewModel(application), SplashContract.ViewModel {

    private val userLiveData = MutableLiveData<FirebaseUser?>()

    override val user: MutableLiveData<FirebaseUser?> = userLiveData
    override val tryAgainVisibility = ObservableInt(View.GONE)

    override fun autoLogin() {
        showProgressBar()
        tryAgainVisibility.set(View.GONE)
        schedulerProvider
            .async(getCurrentUser())
            .subscribe({ firebaseUser ->
                hideProgressBar()
                userLiveData.value = firebaseUser
            }, { throwable ->
                Timber.d(throwable)
                hideProgressBar()
                userLiveData.value= null
            })
            .addTo(compositeDisposable)
    }

    private fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentUser()

    override fun enableTryAgain() {
        tryAgainVisibility.set(View.VISIBLE)
    }
}