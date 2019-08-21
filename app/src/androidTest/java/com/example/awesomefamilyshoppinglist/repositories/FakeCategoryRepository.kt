package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Category
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

open class FakeCategoryRepository : CategoryRepository {

    val category = Category("/1", "fruits", Date())

    override var categories: List<Category> = listOf(category)

    override fun getCategories(familyUid: String, observeOn: Scheduler): Single<List<Category>> =
        Single.just(categories)

}