package com.example.awesomefamilyshoppinglist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.ItemsRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Scheduler

open class MainUseCasesImpl(
    private val userRepository: UserRepository,
    private val familyRepository: FamilyRepository,
    private val categoryRepository: CategoryRepository,
    private val itemsRepository: ItemsRepository
) : MainContract.UseCases {

    private val internalItemsLiveData = MutableLiveData<MainHashMap>()
    override val itemsLiveData: LiveData<MainHashMap> = internalItemsLiveData
    override val firebaseUserLiveData: LiveData<FirebaseUser> = userRepository.firebaseUserLiveData

    override fun loadItems(scheduler: Scheduler): Completable {
        return familyRepository.getCurrentFamily()
            .flatMap { family ->
                itemsRepository.getItems(
                    family.uid,
                    userRepository.familyUsers,
                    categoryRepository.categories,
                    scheduler
                )
            }.toObservable()
            .concatMapIterable { x -> x }
            .reduce(MainHashMap(), { map, item ->
                map.put(CategoryItem(SectionHeaderViewModel(item.category)), ItemItem(ItemViewModel(item)))
                map
            })
            .doOnSuccess { map ->
                internalItemsLiveData.postValue(map)
            }.ignoreElement()
    }

    override fun logout(): Completable = userRepository.logout()
}

class MainHashMap : HashMap<CategoryItem, ArrayList<ItemItem>>() {
    fun put(categoryItem: CategoryItem, item: ItemItem) {
        if (containsKey(categoryItem)) {
            get(categoryItem)!!.add(item)
        } else {
            val a = arrayListOf(item)
            put(categoryItem, a)
        }
    }
}