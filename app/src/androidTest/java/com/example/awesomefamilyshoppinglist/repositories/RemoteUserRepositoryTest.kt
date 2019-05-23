package com.example.awesomefamilyshoppinglist.repositories

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Single
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    lateinit var userRepository: UserRepository

    @Before
    fun before() {
        userRepository = UserRepositoryImpl(FirebaseAuth.getInstance())
    }

    @Test
    fun assertReturnTypes() {
        assertThat(userRepository.getCurrentFirebaseUser(), Matchers.isA(Single::class.java))
        assertThat(userRepository.getSignInIntent(), Matchers.isA(Intent::class.java))
        assertThat(userRepository.logout(), Matchers.isA(Completable::class.java))
    }
}