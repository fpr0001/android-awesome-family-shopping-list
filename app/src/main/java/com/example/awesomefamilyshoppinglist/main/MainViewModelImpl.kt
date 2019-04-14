package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.showToast
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

internal class MainViewModelImpl(
    application: Application,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(application), MainContract.ViewModel {

    private val userLiveData = MutableLiveData<FirebaseUser?>()

    override val version = "v" + BuildConfig.VERSION_NAME

    override fun loadUser() {
        schedulerProvider
            .async(getCurrentUser())
            .subscribe({ firebaseUser ->
                hideProgressBar()
                userLiveData.value = firebaseUser
            }, { throwable ->
                Timber.d(throwable)
                hideProgressBar()
                application().showToast(R.string.failed_to_load_user)
            })
            .addTo(compositeDisposable)
    }

    private fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentUser()

    override fun logout() {
        showProgressBar()
        schedulerProvider
            .async(userRepository.logout())
            .subscribe({
                hideProgressBar()
                userLiveData.value = null
            }, { throwable ->
                Timber.d(throwable)
                hideProgressBar()
                userLiveData.value = null
            })
            .addTo(compositeDisposable)
    }

    override val user: MutableLiveData<FirebaseUser?> = userLiveData

}