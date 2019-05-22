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
                        // TODO on error, move to screen to select family, and there post User
                    }.flatMap { user ->
                        if (user.families.isNotEmpty()) {
                            familyRepository.getFamily(user.families[0], observeOn)
                        } else {
                            Single.error(RuntimeException("User has no family"))
                            // TODO on error, move to screen to select family, and there post User
                        }
                    }.flatMapCompletable { family ->
                        Completable.fromAction {
                            familyRepository.currentFamily = family
                        }
                    }
            }.subscribe({
                hideProgressBar()
                statusLiveData.value = SplashContract.STATUS.LoggedInComplete
            }, { throwable ->
                hideProgressBar()
                if ((throwable as? FirebaseFirestoreException)?.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                    statusLiveData.value = SplashContract.STATUS.LoggedInNoFamily
                } else {
                    Timber.e(throwable)
                    statusLiveData.value = SplashContract.STATUS.LoggedOut
                }
            })
            .addTo(compositeDisposable)
    }

    override fun enableTryAgain() {
        tryAgainVisibility.set(View.VISIBLE)
    }
}


