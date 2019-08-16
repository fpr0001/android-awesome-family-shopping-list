package com.example.awesomefamilyshoppinglist.splash

import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Family
import com.example.awesomefamilyshoppinglist.model.RemoteUser
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.Function3

open class SplashUseCasesImpl(
    private val userRepository: UserRepository,
    private val familyRepository: FamilyRepository,
    private val categoryRepository: CategoryRepository
) : SplashContract.UseCases {

    override fun fetchAndStoreEntities(observeOn: Scheduler): Completable {
        return fetchFirebaseUser()
            .flatMap(fetchUser(observeOn))
            .flatMap { user ->
                if (user.families.isNotEmpty()) {
                    val familyDocument = user.families[0] //TODO support multiple families in the future
                    fetchEntities(familyDocument, observeOn)
                } else {
                    Single.error(SplashContract.Exception(SplashContract.Status.StatusUserHasNoFamily))
                }
            }
            .flatMapCompletable { triple -> storeEntities(triple) }
    }

    private fun storeEntities(triple: Triple<Family, List<User>, List<Category>>) =
        Completable.fromAction {
            familyRepository.currentFamily = triple.first
            userRepository.familyUsers = triple.second
            categoryRepository.categories = triple.third
        }

    private fun fetchFirebaseUser(): Single<FirebaseUser> {
        return userRepository
            .getCurrentFirebaseUser()
            .onErrorResumeNext(Single.error(SplashContract.Exception(SplashContract.Status.StatusLoggedOut)))
    }

    private fun fetchUser(observeOn: Scheduler): (FirebaseUser) -> Single<RemoteUser> {
        return { firebaseUser ->
            userRepository.getRemoteUser(firebaseUser.uid, observeOn)
                .onErrorResumeNext { throwable ->
                    if ((throwable as? FirebaseFirestoreException)?.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                        return@onErrorResumeNext Single.error(SplashContract.Exception(SplashContract.Status.StatusUserNotOnDb))
                    } else {
                        return@onErrorResumeNext Single.error(throwable)
                    }
                }
        }
    }

    private fun fetchEntities(
        familyDocument: DocumentReference,
        observeOn: Scheduler
    ): Single<Triple<Family, List<User>, List<Category>>> {
        return Single.zip<Family, List<User>, List<Category>, Triple<Family, List<User>, List<Category>>>(
            familyRepository.getFamily(familyDocument, observeOn),
            userRepository.getUsersFromFamily(familyDocument, observeOn),
            categoryRepository.getCategories(familyDocument.id, observeOn),
            Function3 { family, users, categories -> Triple(family, users, categories) }
        )
    }
}