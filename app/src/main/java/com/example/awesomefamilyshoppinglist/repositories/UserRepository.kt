package com.example.awesomefamilyshoppinglist.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.model.Family
import com.example.awesomefamilyshoppinglist.model.RemoteUser
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleEmitter
import timber.log.Timber
import java.util.*


interface UserRepository {
    val firebaseUserLiveData: LiveData<User>
    var familyUsers: List<User>
    fun getCurrentFirebaseUser(): Single<User>
    fun getRemoteUser(userUid: String, observeOn: Scheduler): Single<RemoteUser>
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

    private val internalFirebaseUserLiveData = MutableLiveData<User>()
    override var familyUsers = listOf<User>()
    override val firebaseUserLiveData: LiveData<User> = internalFirebaseUserLiveData

    override fun getCurrentFirebaseUser(): Single<User> = Single.create { e: SingleEmitter<User> ->
        val user = userMapper.to(firebaseAuth.currentUser)
        if (user != null) {
            internalFirebaseUserLiveData.postValue(user)//this will get called before user moves from splash screen
            e.onSuccess(user)
        } else e.onError(FirebaseNoSignedInUserException("RemoteUser is null or map to user failed"))
    }

    override fun getRemoteUser(userUid: String, observeOn: Scheduler): Single<RemoteUser> {
        val documentReference = firebaseFirestore
            .collection(COLLECTION_USERS)
            .document(userUid)
        return documentReference.toSingle(observeOn)
    }

    override fun getUsersFromFamily(familyDocument: DocumentReference, observeOn: Scheduler): Single<List<User>> {
        val documentReference = firebaseFirestore
            .collection(COLLECTION_USERS)
            .whereArrayContains(FIELD_FAMILIES, familyDocument)
        return documentReference.toSingle<RemoteUser>(observeOn)
            .map { remoteUsers -> userMapper.to(remoteUsers) }
    }

    //TODO test this func
    override fun logout(): Completable = Completable.create { emitter ->
        val listener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                internalFirebaseUserLiveData.postValue(null)
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
//        val user = firebaseUserLiveData.toUser()
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
            remote.profilePictureUrl
        )
        if (families != null) user.families = families.toMutableList()
        return user
    }

    fun to(firebaseUser: FirebaseUser?): User? {
        return try {
            User(
                "",
                firebaseUser!!.displayName!!,
                Date(firebaseUser.metadata!!.creationTimestamp),
                firebaseUser.email!!,
                firebaseUser.photoUrl?.toString(),
                mutableListOf(),
                firebaseUser.uid
            )
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    fun to(remotes: List<RemoteUser>) = remotes.map { remote -> to(remote) }
}

