package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

object SplashContract {

    interface SplashViewModel {
        fun getCurrentUser() : Single<FirebaseUser>
        fun printAddresses()

//        class Factory(
//            private val application: Application,
//            private val userRepository: UserRepository
//        ) : ViewModelProvider.Factory {
//
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return SplashVMImpl(application, userRepository) as T
//            }
//
//        }
    }

}