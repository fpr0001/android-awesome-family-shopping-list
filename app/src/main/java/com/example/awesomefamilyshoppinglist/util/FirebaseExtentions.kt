package com.example.awesomefamilyshoppinglist.util

import com.example.awesomefamilyshoppinglist.model.BaseRemoteModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.google.firebase.storage.UploadTask
import io.reactivex.*
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

inline fun <reified T : BaseRemoteModel> DocumentReference.toSingle(observeOn: Scheduler) = Single.create<T> { emitter ->
    val eventListener = EventListener<DocumentSnapshot> { documentSnapshot, firebaseFirestoreException ->
        if (firebaseFirestoreException != null) {
            emitter.safeOnError(firebaseFirestoreException)
        } else {
            try {
                val pojo = documentSnapshot!!.toPojo<T>()
                emitter.onSuccess(pojo)
            } catch (e: Throwable) {
                emitter.safeOnError(RuntimeException("Document snapshot is null"))
            }
        }
    }
    val executorScheduler = ExecutorScheduler(observeOn)
    val listenerRegistration = addSnapshotListener(executorScheduler, eventListener)
    emitter.setCancellable {
        executorScheduler.disposable?.dispose()
        listenerRegistration.remove()
    }
}

@Throws(NullPointerException::class)
inline fun <reified T : BaseRemoteModel> DocumentSnapshot.toPojo(): T {
    val pojo = toObject(T::class.java)
    pojo!!.path = reference.path
    return pojo
}

inline fun <reified T : BaseRemoteModel> Query.toSingle(observeOn: Scheduler) = Single.create<List<T>> { emitter ->
    val eventListener = EventListener<QuerySnapshot> { querySnapshot, firebaseFirestoreException ->
        if (firebaseFirestoreException != null) {
            emitter.safeOnError(firebaseFirestoreException)
        } else {
            try {
                val pojos = querySnapshot!!.documents.map { doc -> doc.toPojo<T>() }.toList()
                emitter.onSuccess(pojos)
            } catch (e: Throwable) {
                emitter.safeOnError(RuntimeException("Error parsing objects"))
            }
        }
    }
    val executorScheduler = ExecutorScheduler(observeOn)
    val listenerRegistration = addSnapshotListener(executorScheduler, eventListener)
    emitter.setCancellable {
        executorScheduler.disposable?.dispose()
        listenerRegistration.remove()
    }
}

class ExecutorScheduler(private val scheduler: Scheduler) : Executor {
    var disposable: Disposable? = null
    override fun execute(command: Runnable?) {
        command?.let { disposable = scheduler.scheduleDirect(it) }
    }
}