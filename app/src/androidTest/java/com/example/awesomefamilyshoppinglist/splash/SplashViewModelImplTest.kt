package com.example.awesomefamilyshoppinglist.splash

import android.app.Application
import com.example.awesomefamilyshoppinglist.repositories.UserRepository
import com.example.awesomefamilyshoppinglist.repositories.UserRepositoryImpl
import com.example.awesomefamilyshoppinglist.util.SchedulerProviderTestImpl
import io.reactivex.Single
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SplashViewModelImplTest {

    private lateinit var vm: SplashContract.ViewModel
    private lateinit var userRepository: UserRepository

    @Before
    fun init() {
        val applicationMock = mock(Application::class.java)
        val userRepository = mock(UserRepository::class.java)
        val schedulerProvider = SchedulerProviderTestImpl()
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
        `when`(userRepository.getCurrentUser()).thenReturn(Single.error(RuntimeException()))
        vm.autoLogin()
        assertNull(vm.user.value)
    }


}