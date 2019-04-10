package com.example.awesomefamilyshoppinglist.repositories

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject


interface UserRepository {
    fun getCurrentUser(): Single<FirebaseUser>
    fun getSignInIntent(): Intent
}

class UserRepositoryImpl @Inject constructor() : UserRepository {

    override fun getSignInIntent(): Intent {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }

    override fun getCurrentUser(): Single<FirebaseUser> = Single.create { e: SingleEmitter<FirebaseUser> ->
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            e.onSuccess(user)
        else e.onError(FirebaseNoSignedInUserException("User is null"))

    }

}

