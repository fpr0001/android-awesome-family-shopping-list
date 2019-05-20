package com.example.awesomefamilyshoppinglist.model

import com.google.firebase.auth.FirebaseUser

data class Family(
    var name: String,
    var createdAt: Long,
    var categories: MutableSet<Category>
)

data class Category(
    var name: String,
    var createdAt: Long
)

data class User(
    var name: String,
    var createdAt: Long,
    var email: String,
    var families: MutableSet<Family>,
    var profilePitureUrl: String?
)

fun FirebaseUser.toUser() = User(displayName!!, System.currentTimeMillis(), email!!, mutableSetOf(), photoUrl?.toString())

data class Item(
    var name: String,
    var createdAt: Long,
    var createdBy: User,
    var boughtBy: User?,
    var boughtAt: Long,
    var image: Any,
    var category: Category
)

