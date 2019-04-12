package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.*
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Provider


object SplashContract {

    internal class ViewModelImpl(
        application: Application,
        private val userRepository: UserRepository
    ) : BaseViewModel(application), ViewModel {

        private val userLiveData = MutableLiveData<FirebaseUser>()
        private val loginIntentLiveData = MutableLiveData<Intent>()
        private val buttonTryAgainVisibility = ObservableInt(View.GONE)

        override val loginIntent: MutableLiveData<Intent> = loginIntentLiveData
        override val user: MutableLiveData<FirebaseUser> = userLiveData
        override val tryAgainVisibility = buttonTryAgainVisibility

        override fun autoLogin() {
            showProgressBar()
            buttonTryAgainVisibility.set(View.GONE)
            getCurrentUser()
                .async()
                .subscribe({ firebaseUser ->
                    hideProgressBar()
                    userLiveData.value = firebaseUser
                }, { throwable ->
                    Timber.d(throwable)
                    hideProgressBar()
                    loginIntentLiveData.value = userRepository.getSignInIntent()
                })
                .addTo(compositeDisposable)
        }

        private fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentUser()

        override fun enableTryAgain() {
            buttonTryAgainVisibility.set(View.VISIBLE)
        }

    }

    interface ViewModel : BaseViewModelI {

        val loginIntent: MutableLiveData<Intent>
        val user: MutableLiveData<FirebaseUser>
        val tryAgainVisibility: ObservableInt
        fun autoLogin()
        fun enableTryAgain()

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


