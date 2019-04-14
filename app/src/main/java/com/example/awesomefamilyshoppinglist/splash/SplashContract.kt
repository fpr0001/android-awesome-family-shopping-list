package com.example.awesomefamilyshoppinglist.splash

import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.awesomefamilyshoppinglist.util.BaseViewModelI
import com.google.firebase.auth.FirebaseUser
import javax.inject.Provider

object SplashContract {

    interface Router {

        companion object {
            const val CODE_SIGN_IN = 1
        }

        fun goToMain()
        fun goToLogin()
    }

    interface ViewModel : BaseViewModelI {

        val user: MutableLiveData<FirebaseUser?>
        val tryAgainVisibility: ObservableInt
        fun autoLogin()
        fun enableTryAgain()

        companion object {

            @Suppress("UNCHECKED_CAST")
            internal class Factory(private val provider: Provider<SplashViewModelImpl>) : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return provider.get() as T
                }

                fun getViewModel(activity: FragmentActivity): ViewModel {
                    return ViewModelProviders.of(activity, this).get(SplashViewModelImpl::class.java)
                }
            }
        }
    }
}



