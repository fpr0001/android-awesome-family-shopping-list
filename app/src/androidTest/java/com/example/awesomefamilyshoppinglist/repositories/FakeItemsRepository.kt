package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.model.User
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.*

open class FakeItemsRepository : ItemsRepository {

    val fakeUserRepository = FakeUserRepository()
    val fakeCategoryRepository = FakeCategoryRepository()

    val item = Item(
        "/2",
        "banana",
        Date(),
        fakeUserRepository.fakeCurrentUser,
        null,
        null,
        null,
        fakeCategoryRepository.category
    )

    val item2 = Item(
        "/7",
        "apple",
        Date(),
        fakeUserRepository.fakeCurrentUser,
        null,
        null,
        null,
        fakeCategoryRepository.category
    )

    override val items: List<Item> = listOf(item, item2)

    override fun getItems(
        familyUid: String,
        familyUsers: List<User>,
        categories: List<Category>,
        observeOn: Scheduler
    ): Single<List<Item>> = Single.just(items)

}