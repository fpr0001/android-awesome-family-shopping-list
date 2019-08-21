package com.example.awesomefamilyshoppinglist.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.model.User
import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.ItemsRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Scheduler

open class MainUseCasesImpl(
    private val userRepository: UserRepository,
    private val familyRepository: FamilyRepository,
    private val categoryRepository: CategoryRepository,
    private val itemsRepository: ItemsRepository
) : MainContract.UseCases {

    private val internalItemsLiveData = MutableLiveData<ArrayList<BaseItemViewModel>>()
    override val itemsLiveData: LiveData<ArrayList<BaseItemViewModel>> = internalItemsLiveData
    override val firebaseUserLiveData: LiveData<User> = userRepository.firebaseUserLiveData

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
            .reduce(ArrayList<BaseItemViewModel>(), { list, item ->
                val cm = CategoryViewModel(item.category)
                if (!list.contains(cm)) {
                    list.add(cm)
                }
                list.add(ItemViewModel(item))
                list
            })
            .doOnSuccess { map ->
                internalItemsLiveData.postValue(map)
            }.ignoreElement()
    }

    override fun logout(): Completable = userRepository.logout()
}