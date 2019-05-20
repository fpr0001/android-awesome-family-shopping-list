package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Family
import io.reactivex.Single

interface FamilyRepository {
    fun getCurrentFamily(): Single<Family>
}

open class FamilyRepositoryImpl() : FamilyRepository {

    override fun getCurrentFamily(): Single<Family> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
