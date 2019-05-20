package com.example.awesomefamilyshoppinglist.splash

import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.awesomefamilyshoppinglist.util.BaseViewModelI
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import javax.inject.Provider

object SplashContract {

    interface Router {

        companion object {
            const val CODE_SIGN_IN = 1
        }

        fun goToMain(activity: FragmentActivity)
        fun goToLogin(activity: FragmentActivity)
    }

    interface ViewModel : BaseViewModelI {

        val user: MutableLiveData<FirebaseUser?>
        val tryAgainVisibility: ObservableInt
        fun autoLogin()
        fun enableTryAgain()
        fun uploadCurrentUser()
    }

    @Suppress("UNCHECKED_CAST")
    open class ViewModelFactory(private val provider: Provider<SplashViewModelImpl>) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return provider.get() as T
        }

        fun getViewModel(activity: FragmentActivity): ViewModel {
            return ViewModelProviders.of(activity, this).get(SplashViewModelImpl::class.java)
        }
    }
}



