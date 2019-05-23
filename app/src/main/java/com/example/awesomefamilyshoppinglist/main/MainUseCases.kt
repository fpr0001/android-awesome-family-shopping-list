package com.example.awesomefamilyshoppinglist.main

import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.ItemsRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import io.reactivex.Scheduler

//open class MainUseCasesImpl(
//    private val userRepository: UserRepository,
//    private val familyRepository: FamilyRepository,
//    private val categoryRepository: CategoryRepository,
//    private val itemsRepository: ItemsRepository
//) : MainContract.MainUseCases {
//
//    fun fetchItems(observeOn: Scheduler) =
//
//            .flatmap
//}