package com.example.awesomefamilyshoppinglist.repositories

import android.content.Intent
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleEmitter


interface UserRepository {
    fun getCurrentFirebaseUser(): Single<FirebaseUser>
    fun getUser(userUid: String, observeOn: Scheduler): Single<User>
    fun getSignInIntent(): Intent
    fun logout(): Completable
    fun uploadUser(firebaseUser: FirebaseUser): Completable
}

open class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRepository {

    private val COLLECTION = "users"

    override fun getCurrentFirebaseUser(): Single<FirebaseUser> = Single.create { e: SingleEmitter<FirebaseUser> ->
        val user = firebaseAuth.currentUser
        if (user != null)
            e.onSuccess(user)
        else e.onError(FirebaseNoSignedInUserException("User is null"))
    }

    override fun getUser(userUid: String, observeOn: Scheduler): Single<User> {
        val documentReference = firebaseFirestore
            .collection(COLLECTION)
            .document(userUid)
        return documentReference.toSingle<User>(observeOn)
    }

    override fun getSignInIntent(): Intent {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }

    //TODO test this func
    override fun logout(): Completable = Completable.create { emitter ->
        val idTokenListener = object : FirebaseAuth.IdTokenListener {
            val removeTokenListenerAction = { firebaseAuth.removeIdTokenListener(this) }
            override fun onIdTokenChanged(p0: FirebaseAuth) {
                removeTokenListenerAction()
                emitter.onComplete()
            }
        }
        firebaseAuth.addIdTokenListener(idTokenListener)
        firebaseAuth.signOut()
    }

    override fun uploadUser(firebaseUser: FirebaseUser): Completable {
//        val user = firebaseUser.toUser()
        return Completable.complete()
        //TODO here
    }
}

