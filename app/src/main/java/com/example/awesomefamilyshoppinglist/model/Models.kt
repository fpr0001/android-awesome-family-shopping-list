package com.example.awesomefamilyshoppinglist.model

import com.google.firebase.auth.FirebaseUser

data class Family(var name: String, var createdAt: Long)

data class Category(var name: String, var createdAt: Long)

//data class User(var firebaseUser: FirebaseUser, )

data class Item(
    var name: String,
    var createdAt: Long,
    var createdBy: FirebaseUser,
    var boughtBy: FirebaseUser,
    var boughtAt: Long,
    var image: Any,
    var category: Category
)

