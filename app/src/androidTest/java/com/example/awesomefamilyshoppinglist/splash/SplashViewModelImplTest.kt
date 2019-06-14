package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderTestImpl
import com.example.awesomefamilyshoppinglist.utils.any
import io.reactivex.Completable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SplashViewModelImplTest {

    private lateinit var vm: SplashContract.ViewModel
    private lateinit var splashUseCases: SplashContract.SplashUseCases

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val applicationMock = mock(Application::class.java)
        val schedulerProvider = SchedulerProviderTestImpl()
        splashUseCases = mock(SplashUseCasesImpl::class.java)
        vm = SplashViewModelImpl(applicationMock, splashUseCases, schedulerProvider)
    }

    @Test
    fun auto_login_without_user() {
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.error(RuntimeException()))
        vm.autoLogin()
        assertNotEquals(SplashContract.Status.StatusLoggedIn, vm.statusLiveData.value)
    }

    @Test
    fun auto_login_with_user() {
        `when`(splashUseCases.fetchAndStoreEntities(any())).thenReturn(Completable.complete())
        vm.autoLogin()
        assertEquals(vm.statusLiveData.value, SplashContract.Status.StatusLoggedIn)
    }

    @Test
    fun enable_try_again_button() {
        vm.enableTryAgain()
        assertEquals(vm.tryAgainVisibility.get(), View.VISIBLE)
    }
}