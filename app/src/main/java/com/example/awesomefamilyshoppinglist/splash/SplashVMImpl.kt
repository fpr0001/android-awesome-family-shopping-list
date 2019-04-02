package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

class SplashVMImpl(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application), SplashContract.SplashViewModel {

    override fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentUser()

}