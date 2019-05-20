package com.example.awesomefamilyshoppinglist.util

import com.example.awesomefamilyshoppinglist.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.UploadTask
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Cancellable
import java.lang.RuntimeException
import java.util.concurrent.Executor

fun UploadTask.toCompletable() = Completable.create { emitter ->
    val onSuccessListener = OnSuccessListener<UploadTask.TaskSnapshot> { emitter.onComplete() }
    val onFailureListener = OnFailureListener { if (!emitter.isDisposed) emitter.onError(it) }
    val cancellable = Cancellable {
        removeOnSuccessListener(onSuccessListener)
        removeOnFailureListener(onFailureListener)
        cancel()
    }
    addOnSuccessListener(onSuccessListener)
    addOnFailureListener(onFailureListener)
    emitter.setCancellable(cancellable)
}


inline fun <reified T> DocumentReference.toSingle(executor: Executor) = Single.create<T> { emitter ->
    val eventListener = EventListener<DocumentSnapshot> { documentSnapshot, firebaseFirestoreException ->
        if (firebaseFirestoreException != null) {
            emitter.safeOnError(firebaseFirestoreException)
        } else {
            val pojo = documentSnapshot?.toObject(T::class.java)
            if (pojo != null) {
                emitter.onSuccess(pojo)
            } else {
                emitter.safeOnError(RuntimeException("Error parsing object"))
            }
        }
    }
    val listenerRegistration = addSnapshotListener(executor, eventListener)
    emitter.setCancellable {
        listenerRegistration.remove()
    }
}