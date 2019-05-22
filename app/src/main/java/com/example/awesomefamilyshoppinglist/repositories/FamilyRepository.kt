package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Family
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith

interface FamilyRepository {
    var currentFamily: Family?
    fun getFamily(documentReference: DocumentReference, observeOn: Scheduler): Single<Family>
    fun getCategories(familyUid: String, observeOn: Scheduler): Single<List<Category>>

}

open class FamilyRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : FamilyRepository {

    private val COLLECTION = "family/%s/categories"

    override var currentFamily: Family? = null

    override fun getFamily(documentReference: DocumentReference, observeOn: Scheduler) =
        documentReference.toSingle<Family>(observeOn)
            .flatMap { family ->
                getCategories(documentReference.id, observeOn)
                    .map {
                        family.categories.addAll(it)
                        family
                    }
            }

    override fun getCategories(familyUid: String, observeOn: Scheduler) =
        firebaseFirestore
            .collection(String.format(COLLECTION, familyUid))
            .toSingle<Category>(observeOn)
}
