package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.lang.RuntimeException

open class SplashViewModelImpl(
    application: Application,
    private val userRepository: UserRepository,
    private val familyRepository: FamilyRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(application), SplashContract.ViewModel {

    override val statusLiveData: MutableLiveData<SplashContract.STATUS> =
        MutableLiveData(SplashContract.STATUS.LoggedOut)
    override val tryAgainVisibility = ObservableInt(View.GONE)

    override fun autoLogin() {
        showProgressBar()
        tryAgainVisibility.set(View.GONE)
        schedulerProvider
            .async { observeOn ->
                userRepository
                    .getCurrentFirebaseUser()
                    .flatMap { firebaseUser ->
                        userRepository.getUser(firebaseUser.uid, observeOn)
//                            .onErrorResumeNext() ON ERROR NO USER WITH THAT ID, POST NEW USER
                    }.flatMap { user ->
                        if (user.families.isNotEmpty()) {
                            familyRepository.getFamily(user.families[0], observeOn)
                        } else {
                            Single.error(RuntimeException("User has no family"))
                        }
                    }.flatMapCompletable { family ->
                        Completable.fromAction { familyRepository.currentFamily = family }
                    }
            }.subscribe({
                hideProgressBar()
                statusLiveData.value = SplashContract.STATUS.LoggedIn
            }, { throwable ->
                Timber.e(throwable)
                hideProgressBar()
                statusLiveData.value = SplashContract.STATUS.LoggedOut
            })
            .addTo(compositeDisposable)
    }

    override fun enableTryAgain() {
        tryAgainVisibility.set(View.VISIBLE)
    }
}

