package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.model.Category
import com.example.awesomefamilyshoppinglist.model.RemoteCategory
import com.example.awesomefamilyshoppinglist.util.toSingle
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Scheduler
import io.reactivex.Single

interface CategoryRepository {
    var categories: List<Category>
    fun getCategories(familyUid: String, observeOn: Scheduler): Single<List<Category>>
}

open class CategoryRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {

    private val COLLECTION_CATEGORIES = "family/%s/categories"

    override var categories: List<Category> = listOf()

    override fun getCategories(familyUid: String, observeOn: Scheduler) =
        firebaseFirestore
            .collection(String.format(COLLECTION_CATEGORIES, familyUid))
            .toSingle<RemoteCategory>(observeOn)
            .map { remoteCategories -> categoryMapper.to(remoteCategories) }
}

open class CategoryMapper {
    fun to(remote: RemoteCategory): Category {
        return Category(remote.path, remote.name, remote.createdAt.toDate())
    }

    fun to(remotes: List<RemoteCategory>): List<Category> {
        return remotes.map { remote -> to(remote) }
    }
}
