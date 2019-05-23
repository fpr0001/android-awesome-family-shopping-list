package com.example.awesomefamilyshoppinglist.repositories

import android.content.Intent
import com.example.awesomefamilyshoppinglist.model.Family
import com.example.awesomefamilyshoppinglist.model.RemoteUser
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleEmitter


interface UserRepository {
    var familyUsers: List<User>
    fun getCurrentFirebaseUser(): Single<FirebaseUser>
    fun getRemoteUser(userUid: String, observeOn: Scheduler): Single<RemoteUser>
    fun getSignInIntent(): Intent
    fun logout(): Completable
    fun uploadUser(firebaseUser: FirebaseUser): Completable
    fun getUsersFromFamily(familyDocument: DocumentReference, observeOn: Scheduler): Single<List<User>>
}

open class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val userMapper: UserMapper
) : UserRepository {

    private val COLLECTION_USERS = "users"
    private val FIELD_FAMILIES = "families"
    private val PATH_FAMILY = "/family/%s"

    override var familyUsers = listOf<User>()

    override fun getCurrentFirebaseUser(): Single<FirebaseUser> = Single.create { e: SingleEmitter<FirebaseUser> ->
        val user = firebaseAuth.currentUser
        if (user != null)
            e.onSuccess(user)
        else e.onError(FirebaseNoSignedInUserException("RemoteUser is null"))
    }

    override fun getRemoteUser(userUid: String, observeOn: Scheduler): Single<RemoteUser> {
        val documentReference = firebaseFirestore
            .collection(COLLECTION_USERS)
            .document(userUid)
        return documentReference.toSingle<RemoteUser>(observeOn)
    }

    override fun getUsersFromFamily(familyDocument: DocumentReference, observeOn: Scheduler): Single<List<User>> {
        val documentReference = firebaseFirestore
            .collection(COLLECTION_USERS)
            .whereArrayContains(FIELD_FAMILIES, familyDocument)
        return documentReference.toSingle<RemoteUser>(observeOn)
            .map { remoteUsers -> userMapper.to(remoteUsers) }
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
        val listener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                emitter.onComplete()
            }
        }
        emitter.setCancellable {
            firebaseAuth.removeAuthStateListener(listener)
        }
        firebaseAuth.addAuthStateListener(listener)
        firebaseAuth.signOut()
    }

    override fun uploadUser(firebaseUser: FirebaseUser): Completable {
//        val user = firebaseUser.toUser()
        return Completable.complete()
        //TODO here
    }
}

open class UserMapper {
    fun to(remote: RemoteUser, families: List<Family>? = null): User {
        val user = User(
            remote.path,
            remote.name,
            remote.createdAt.toDate(),
            remote.email,
            remote.profilePitureUrl
        )
        if (families != null) user.families = families.toMutableList()
        return user
    }

    fun to(remotes: List<RemoteUser>) = remotes.map { remote -> to(remote) }
}

