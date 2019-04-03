package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.awesomefamilyshoppinglist.App
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

class SplashVMImpl(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application), SplashContract.SplashViewModel {

    override fun printAddresses() {
        println(getApplication<App>())
        println(userRepository)
        println(this)
    }

    override fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentUser()

}