package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.Executor

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
            .async(getCurrentUser())
            .subscribe({
                hideProgressBar()
                fetchUser(it.uid)
//                statusLiveData.value = SplashContract.STATUS.LoggedIn
            }, { throwable ->
                Timber.e(throwable)
                hideProgressBar()
                statusLiveData.value = SplashContract.STATUS.LoggedOut
            })
            .addTo(compositeDisposable)
    }

    fun fetchUser(userUid: String) {
        showProgressBar()
        schedulerProvider.async { observeOn ->
            userRepository.getUser(userUid, observeOn)
                .flatMap { user -> familyRepository.getFamily(user.families[0], observeOn) }
                .map { family -> familyRepository.currentFamily = family }
        }
            .subscribe({ user ->
                hideProgressBar()
                println(user.toString())
            }, { throwable ->
                Timber.e(throwable)
                hideProgressBar()
            })
            .addTo(
                compositeDisposable
            )
        //TODO fetch user, save it's family and categories
        //TODO if no user, post it (ignore this part for now)
    }

    override fun uploadCurrentUser() {
        autoLogin()
//        showProgressBar()
//        schedulerProvider
//            .async(getCurrentUser()
//                .flatMap {
//                    fetchUser(it.uid)
////                        .toSingleDefault(it)
//                })
//            .subscribe({ firebaseUser ->
//                hideProgressBar()
//                userLiveData.value = firebaseUser
//            }, { throwable ->
//                Timber.d(throwable)
//                hideProgressBar()
//                userLiveData.value = null
//            })
//            .addTo(compositeDisposable)
    }

    private fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentFirebaseUser()

    override fun enableTryAgain() {
        tryAgainVisibility.set(View.VISIBLE)
    }
}

