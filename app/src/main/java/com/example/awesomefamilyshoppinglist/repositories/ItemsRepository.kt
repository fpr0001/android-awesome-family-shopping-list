package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.model.RemoteItem
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Scheduler
import io.reactivex.Single

interface ItemsRepository {
    val items: MutableList<Item>
    fun getItems(
        familyUid: String,
        familyUsers: List<User>,
        categories: List<Category>,
        observeOn: Scheduler
    ): Single<List<Item>>
}

open class ItemsRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val itemMapper: ItemMapper
) : ItemsRepository {

    private val COLLECTION = "family/%s/items"

    override val items: MutableList<Item> = mutableListOf()

    override fun getItems(
        familyUid: String,
        familyUsers: List<User>,
        categories: List<Category>,
        observeOn: Scheduler
    ) = firebaseFirestore
        .collection(String.format(COLLECTION, familyUid))
        .whereGreaterThan("createdAt", Timestamp(51840000, 0)) //two months old
        .toSingle<RemoteItem>(observeOn)
        .map { items -> itemMapper.to(items, familyUsers, categories) }
}

open class ItemMapper {

    @Throws(NullPointerException::class)
    fun to(remote: RemoteItem, familyUsers: List<User>, categories: List<Category>) : Item {
        val findUserPredicate = { userUid: String -> familyUsers.find { user: User -> user.uid == userUid }!! }
        val item = Item(
            remote.path,
            remote.name,
            remote.createdAt.toDate(),
            findUserPredicate(remote.createdBy.id),
            remote.boughtBy?.let { findUserPredicate(it.id) },
            remote.boughtAt?.toDate(),
            null,
            categories.find { category -> category.uid == remote.category.id }!!
        )
        return item
    }

    @Throws(NullPointerException::class)
    fun to(remotes: List<RemoteItem>, familyUsers: List<User>, categories: List<Category>) : List<Item> {
        return remotes.map { remote -> to(remote, familyUsers, categories) }
    }
}