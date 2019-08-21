package com.example.awesomefamilyshoppinglist.splash

import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStoreOwner
import com.example.awesomefamilyshoppinglist.util.BaseViewModelI
import io.reactivex.Completable
import io.reactivex.Scheduler
import java.lang.RuntimeException
import javax.inject.Provider

object SplashContract {

    interface Router {

        companion object {
            const val CODE_SIGN_IN = 1
        }

        fun goToMain(activity: FragmentActivity)
        fun goToLogin(activity: FragmentActivity)

        @VisibleForTesting
        fun getSignInIntent(): Intent
    }

    interface ViewModel : BaseViewModelI {
        val statusLiveData: MutableLiveData<Status>
        val tryAgainVisibility: ObservableInt
        fun autoLogin()
        fun enableTryAgain()
    }

    @Suppress("UNCHECKED_CAST")
    open class ViewModelFactory(private val provider: Provider<SplashViewModelImpl>) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return provider.get() as T
        }

        fun getViewModel(storeOwner: ViewModelStoreOwner): ViewModel {
            return ViewModelProvider(storeOwner, this).get(SplashViewModelImpl::class.java)
        }
    }

    interface UseCases {
        fun fetchAndStoreEntities(observeOn: Scheduler): Completable
    }

    open class Status {
        object StatusLoggedIn : Status()
        object StatusUserHasNoFamily : Status()
        object StatusUserNotOnDb : Status() //remote db
        object StatusLoggedOut : Status()
        object StatusUnexpectedError : Status()
    }

    class Exception(val status: Status) : RuntimeException()
}



