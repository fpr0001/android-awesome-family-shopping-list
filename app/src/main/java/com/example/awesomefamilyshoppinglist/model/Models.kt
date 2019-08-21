package com.example.awesomefamilyshoppinglist.model

import java.util.*

open class BaseModel(
    var path: String,
    var name: String,
    val createdAt: Date,
    open val uid: String = path.split("/".toRegex()).last()
)

class Item(
    path: String,
    name: String,
    createdAt: Date,
    var createdBy: User,
    var boughtBy: User? = null,
    var boughtAt: Date? = null,
    var image: Any? = null,
    var category: Category
) :
    BaseModel(path, name, createdAt)

class Category(path: String, name: String, createdAt: Date) : BaseModel(path, name, createdAt)

class Family(
    path: String,
    name: String,
    createdAt: Date
) :
    BaseModel(path, name, createdAt)

class User(
    path: String,
    name: String,
    createdAt: Date,
    var email: String,
    var profilePictureUrl: String? = null,
    var families: MutableList<Family> = mutableListOf(),
    uid: String = path.split("/".toRegex()).last()
) :
    BaseModel(path, name, createdAt, uid)















