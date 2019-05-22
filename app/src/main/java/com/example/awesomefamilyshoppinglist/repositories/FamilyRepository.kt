package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Family
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Scheduler
import io.reactivex.Single

interface FamilyRepository {
    var currentFamily: Family?
    fun getFamily(documentReference: DocumentReference, observeOn: Scheduler): Single<Family>
}

open class FamilyRepositoryImpl : FamilyRepository {
    override var currentFamily: Family? = null

    override fun getFamily(documentReference: DocumentReference, observeOn: Scheduler) =
        documentReference.toSingle<Family>(observeOn)
}
