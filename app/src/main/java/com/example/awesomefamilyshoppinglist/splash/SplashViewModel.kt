package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.awesomefamilyshoppinglist.App
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import javax.inject.Provider


object SplashContract {

    internal class ViewModelImpl(
        application: Application,
        private val userRepository: UserRepository
    ) : BaseViewModel(application), ViewModel {

        private val userLiveData = MutableLiveData<FirebaseUser>()
        private val loginIntentLiveData = MutableLiveData<Intent>()

        override val loginIntent: MutableLiveData<Intent> = loginIntentLiveData
        override val user: MutableLiveData<FirebaseUser> = userLiveData

        override fun autoLogin() {
            getCurrentUser()
                .subscribe({firebaseUser ->
                    userLiveData.value = firebaseUser
                }, {throwable ->
                    loginIntentLiveData.value = userRepository.getSignInIntent()
                })
        }



        private fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentUser()


    }

    interface ViewModel {

        fun autoLogin()
        val loginIntent: MutableLiveData<Intent>
        val user: MutableLiveData<FirebaseUser>

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



