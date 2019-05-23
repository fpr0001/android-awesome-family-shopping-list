package com.example.awesomefamilyshoppinglist.main

import android.app.Application
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
    private val userRepository: UserRepository,
    private val familyRepository: FamilyRepository,
    private val categoryRepository: CategoryRepository,
    private val itemsRepository: ItemsRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(application), MainContract.ViewModel {

    override val user: MutableLiveData<FirebaseUser?> = MutableLiveData()

    override val version = "v" + BuildConfig.VERSION_NAME

    override fun loadUser() {
        schedulerProvider
            .async(getCurrentUser())
            .subscribe({ firebaseUser ->
                hideProgressBar()
                user.value = firebaseUser
            }, { throwable ->
                Timber.e(throwable)
                hideProgressBar()
                application().showToast(R.string.failed_to_load_user)
            })
            .addTo(compositeDisposable)
    }

    private fun getCurrentUser(): Single<FirebaseUser> = userRepository.getCurrentFirebaseUser()

    override fun loadItems() {
        showProgressBar()
        schedulerProvider
            .async<List<Item>> { observeOn ->
                familyRepository.getCurrentFamily()
                    .flatMap { family ->
                        itemsRepository.getItems(
                            family.uid,
                            userRepository.familyUsers,
                            categoryRepository.categories,
                            observeOn)
                    }
            }
            .subscribe({ items ->
                items.toString()
            }, { throwable ->
                Timber.e(throwable)
            })
            .addTo(compositeDisposable)
    }

    override fun logout() {
        showProgressBar()
        schedulerProvider
            .async(userRepository.logout())
            .subscribe({
                hideProgressBar()
                user.value = null
            }, { throwable ->
                Timber.e(throwable)
                hideProgressBar()
                user.value = null
            })
            .addTo(compositeDisposable)
    }
}