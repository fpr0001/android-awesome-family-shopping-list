package com.example.awesomefamilyshoppinglist.main

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.awesomefamilyshoppinglist.util.BaseViewModelI
import com.google.firebase.auth.FirebaseUser
import javax.inject.Provider

object MainContract {

    interface Router {
        fun goToSplash(context: Context)
        fun goToHistory(context: Context)
    }

    interface ViewModel : BaseViewModelI {

        val user: MutableLiveData<FirebaseUser?>
        val version: String
        fun logout()
        fun loadUser()

        companion object {

            @Suppress("UNCHECKED_CAST")
            open class Factory(private val provider: Provider<MainViewModelImpl>) : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return provider.get() as T
                }

                fun getViewModel(activity: FragmentActivity): ViewModel {
                    return ViewModelProviders.of(activity, this).get(MainViewModelImpl::class.java)
                }
            }
        }
    }
}
