package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.*
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Scheduler
import io.reactivex.Single
import java.lang.RuntimeException

interface FamilyRepository {
    var currentFamily: Family?
    fun getCurrentFamily(): Single<Family>
    fun getFamily(documentReference: DocumentReference, observeOn: Scheduler): Single<Family>
}

open class FamilyRepositoryImpl(
    private val familyMapper: FamilyMapper
) : FamilyRepository {

    override var currentFamily: Family? = null

    override fun getCurrentFamily(): Single<Family> =
        if (currentFamily == null)
            Single.error(RuntimeException("currentFamily in FamilyRepository is null"))
        else
            Single.just(currentFamily)

    override fun getFamily(documentReference: DocumentReference, observeOn: Scheduler) =
        documentReference
            .toSingle<RemoteFamily>(observeOn)
            .map { remote -> familyMapper.to(remote) }
}

open class FamilyMapper {
    fun to(remote: RemoteFamily): Family {
        return Family(remote.path, remote.name, remote.createdAt.toDate())
    }
}


