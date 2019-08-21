package com.example.awesomefamilyshoppinglist.main

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.awesomefamilyshoppinglist.util.BaseViewModelI
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Provider

object MainContract {

    interface Router {
        fun goToSplash(context: Context)
        fun goToHistory(context: Context)
    }

    interface UseCases {
        fun loadItems(scheduler: Scheduler): Completable
        fun logout(): Completable
        val firebaseUserLiveData: LiveData<FirebaseUser>
        val itemsLiveData: LiveData<ArrayList<BaseItemViewModel>>
    }

    interface ViewModel : BaseViewModelI {

        val firebaseUserLiveData: LiveData<FirebaseUser>
        val itemsLiveData: LiveData<ArrayList<BaseItemViewModel>>
        val version: String
        fun logout()
        fun loadItems()

        companion object {

            @Suppress("UNCHECKED_CAST")
            open class Factory(private val provider: Provider<MainViewModelImpl>) : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return provider.get() as T
                }

                open fun getViewModel(storeOwner: ViewModelStoreOwner): ViewModel {
                    return ViewModelProvider(storeOwner, this).get(MainViewModelImpl::class.java)
                }
            }
        }
    }
}
