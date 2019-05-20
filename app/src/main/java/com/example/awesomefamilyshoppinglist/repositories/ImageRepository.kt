package com.example.awesomefamilyshoppinglist.repositories

import com.example.awesomefamilyshoppinglist.util.toCompletable
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable

interface ImageRepository {

    fun postUserProfilePicture(firebaseUser: FirebaseUser): Completable
}

open class ImageRepositoryImpl(
    private val firebaseStorage: FirebaseStorage
) : ImageRepository {

    override fun postUserProfilePicture(firebaseUser: FirebaseUser): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


//    private val USERS_DIRECTORY = "images/users_profile_pic/"
//
//    override fun postUserProfilePicture(firebaseUser: FirebaseUser): Completable {
//        return if (firebaseUser.photoUrl == null) {
//            Completable.complete()
//        } else {
//            val storageReference =
//                firebaseStorage.getReferenceFromUrl("$USERS_DIRECTORY${firebaseUser.uid}")
//            //download image here, save temporarily locally, then upload to the firebaseStorage
//            storageReference.putFile(firebaseUser.photoUrl!!).toCompletable()
//        }
//    }
}