package com.example.awesomefamilyshoppinglist.model

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

abstract class BaseModel {
    var name: String = ""
    lateinit var createdAt: Timestamp
}

class Family : BaseModel(), Serializable {
    var categories: MutableList<Category> = mutableListOf()
}

class Category : BaseModel(), Serializable

class User : BaseModel(), Serializable {
    var email: String = ""
    var families: MutableList<DocumentReference> = mutableListOf()
    var profilePitureUrl: String? = null
}

fun FirebaseUser.asNewUser() = User().let {
    it.name = displayName!!
    it.createdAt = Timestamp.now()
    it.email = email!!
    it.profilePitureUrl = photoUrl?.toString()
}

class Item : BaseModel(), Serializable {
    lateinit var createdBy: User
    var boughtBy: User? = null
    var boughtAt: Long = 0
    var image: Any? = null
    lateinit var category: Category
}