package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Family
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

open class FakeFamilyRepository : FamilyRepository {

    val family = Family("/5", "AwesomeFamily", Date())

    override var currentFamily: Family? = family

    override fun getCurrentFamily(): Single<Family> = Single.just(family)

    override fun getFamily(documentReference: DocumentReference, observeOn: Scheduler): Single<Family> =
        Single.just(family)

}