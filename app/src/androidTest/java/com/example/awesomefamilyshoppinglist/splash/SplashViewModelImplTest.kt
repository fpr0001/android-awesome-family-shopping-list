package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderImpl
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderTestImpl
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SplashViewModelImplTest {

    private lateinit var vm: SplashContract.ViewModel
    private lateinit var userRepository: UserRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val applicationMock = mock(Application::class.java)
        val schedulerProvider = SchedulerProviderTestImpl()
        userRepository = mock(UserRepository::class.java)
        vm = SplashViewModelImpl(applicationMock, userRepository, schedulerProvider)
    }

    @Test
    fun auto_login_without_user() {
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        vm.autoLogin()
        assertNull(vm.user.value)
    }

    @Test
    fun auto_login_with_user() {
        val firebaseUser = mock(FirebaseUser::class.java)
        `when`(userRepository.getCurrentUser()).thenReturn(Single.just(firebaseUser))
        vm.autoLogin()
        assertEquals(vm.user.value, firebaseUser)
    }

    @Test
    fun enable_try_again_button() {
        vm.enableTryAgain()
        assertEquals(vm.tryAgainVisibility.get(), View.VISIBLE)
    }
}