package com.example.awesomefamilyshoppinglist.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

abstract class BaseRemoteModel {
    val uid get() = path.split("/".toRegex()).last()
    var path: String = ""
    var name: String = ""
    lateinit var createdAt: Timestamp
}

class RemoteFamily : BaseRemoteModel(), Serializable

class RemoteCategory : BaseRemoteModel(), Serializable

class RemoteUser : BaseRemoteModel(), Serializable {
    var email: String = ""
    var families: MutableList<DocumentReference> = mutableListOf()
    var profilePitureUrl: String? = null
}

//fun FirebaseUser.asNewUser() = RemoteUser().let {
//    it.name = displayName!!
//    it.createdAt = Timestamp.now()
//    it.email = email!!
//    it.profilePitureUrl = photoUrl?.toString()
//}

class RemoteItem : BaseRemoteModel(), Serializable {
    lateinit var createdBy: DocumentReference
    var boughtBy: DocumentReference? = null
    var boughtAt: Timestamp? = null
    var image: Any? = null
    lateinit var category: DocumentReference
}