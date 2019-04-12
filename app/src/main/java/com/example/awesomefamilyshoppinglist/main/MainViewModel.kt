package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.BaseViewModelI
import com.example.awesomefamilyshoppinglist.util.async
import com.example.awesomefamilyshoppinglist.util.showToast
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Provider

object MainContract {

    internal class ViewModelImpl(
        application: Application,
        private val userRepository: UserRepository
    ) : BaseViewModel(application), ViewModel {

        private val userLiveData = MutableLiveData<FirebaseUser?>()

        override val version = "v" + BuildConfig.VERSION_NAME

        override fun loadUser() {
            getCurrentUser()
                .async()
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
            userRepository
                .logout()
                .async()
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

    interface ViewModel : BaseViewModelI {

        val user: MutableLiveData<FirebaseUser?>
        val version: String
        fun logout()
        fun loadUser()

        companion object {

            @Suppress("UNCHECKED_CAST")
            internal class Factory(private val provider: Provider<ViewModelImpl>) : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return provider.get() as T
                }

                fun getViewModel(activity: FragmentActivity): ViewModel {
                    return ViewModelProviders.of(activity, this).get(ViewModelImpl::class.java)
                }
            }

        }
    }
}
