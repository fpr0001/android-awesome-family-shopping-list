package com.example.awesomefamilyshoppinglist.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.model.RemoteUser
import com.example.awesomefamilyshoppinglist.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

open class FakeUserRepository : UserRepository {

    val fakeCurrentUser = User(
        "/4",
        "Felipe",
        Date(),
        "felipe@felipe.com",
        "https://secure.gravatar.com/avatar/da29988e7a47a6808146493cec0814c7",
        mutableListOf(),
        "fpr0001"
    )

    val user = User("/3", "felipe", Date(), "e@e.com", null, mutableListOf())

    val internalFirebaseUserLiveData = MutableLiveData(fakeCurrentUser)

    override val firebaseUserLiveData: LiveData<User> = internalFirebaseUserLiveData
    override var familyUsers: List<User> = listOf(user, user)
    override fun getCurrentFirebaseUser(): Single<User> = Single.just(internalFirebaseUserLiveData.value)
    override fun getRemoteUser(userUid: String, observeOn: Scheduler): Single<RemoteUser> =
        Single.error(RuntimeException("Not implemented"))

    override fun logout(): Completable {
        internalFirebaseUserLiveData.postValue(null)
        return Completable.complete()
    }

    override fun uploadUser(firebaseUser: FirebaseUser): Completable = Completable.complete()

    override fun getUsersFromFamily(familyDocument: DocumentReference, observeOn: Scheduler): Single<List<User>> =
        Single.just(familyUsers)
}