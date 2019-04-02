package com.example.awesomefamilyshoppinglist.splash

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import javax.inject.Inject


interface UserRepository {
    fun getCurrentUser(): Single<FirebaseUser>
    fun getSignInIntent(): Single<Intent>
}

class UserRepositoryImpl @Inject constructor() : UserRepository {

    override fun getSignInIntent(): Single<Intent> {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        return Single.just(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        )
    }

    override fun getCurrentUser(): Single<FirebaseUser> = Single.just(FirebaseAuth.getInstance().currentUser)

}

