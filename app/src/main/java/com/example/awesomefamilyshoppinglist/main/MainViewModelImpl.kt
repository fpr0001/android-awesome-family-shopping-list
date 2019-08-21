package com.example.awesomefamilyshoppinglist.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.awesomefamilyshoppinglist.BuildConfig
import com.example.awesomefamilyshoppinglist.R
import com.example.awesomefamilyshoppinglist.model.Item
import com.example.awesomefamilyshoppinglist.model.RemoteItem
import com.example.awesomefamilyshoppinglist.repositories.CategoryRepository
import com.example.awesomefamilyshoppinglist.repositories.FamilyRepository
import com.example.awesomefamilyshoppinglist.repositories.ItemsRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.BaseViewModel
import com.example.awesomefamilyshoppinglist.util.SchedulerProvider
import com.example.awesomefamilyshoppinglist.util.showToast
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

open class MainViewModelImpl(
    application: Application,
    private val useCases: MainContract.UseCases,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(application), MainContract.ViewModel {

    override val version = "v" + BuildConfig.VERSION_NAME
    override val firebaseUserLiveData: LiveData<FirebaseUser> = useCases.firebaseUserLiveData
    override val itemsLiveData: LiveData<ArrayList<BaseItemViewModel>> = useCases.itemsLiveData

    override fun loadItems() {
        showProgressBar()
        schedulerProvider
            .async { scheduler -> useCases.loadItems(scheduler) }
            .subscribe({
                hideProgressBar()
            }, { throwable ->
                Timber.e(throwable)
                application().showToast(throwable.localizedMessage)
                hideProgressBar()
            })
            .addTo(compositeDisposable)
    }

    override fun logout() {
        showProgressBar()
        schedulerProvider
            .async(useCases.logout())
            .subscribe({
                hideProgressBar()
            }, { throwable ->
                application().showToast(R.string.general_error_message)
                Timber.e(throwable)
                hideProgressBar()
            })
            .addTo(compositeDisposable)
    }

}