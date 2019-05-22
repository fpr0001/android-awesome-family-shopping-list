package com.example.awesomefamilyshoppinglist.util

import com.example.awesomefamilyshoppinglist.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.UploadTask
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Cancellable
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.internal.disposables.SequentialDisposable
import io.reactivex.internal.operators.completable.CompletableObserveOn
import java.lang.RuntimeException
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference

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

inline fun <reified T> DocumentReference.toSingle(observeOn: Scheduler) = Single.create<T> { emitter ->
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