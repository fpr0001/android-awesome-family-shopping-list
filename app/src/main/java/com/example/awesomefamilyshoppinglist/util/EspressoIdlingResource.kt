package com.example.awesomefamilyshoppinglist.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

class EspressoIdlingResource(private val name: String) : IdlingResource {

    companion object {
        private const val globalName = "GLOBAL_IDLING_RESOURCE"
        private val instance: EspressoIdlingResource by lazy {
            EspressoIdlingResource(globalName)
        }

        fun increment() {
            instance.increment()
        }

        fun decrement() {
            instance.decrement()
        }
    }

    private val counter = AtomicInteger(0)

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            resourceCallback?.onTransitionToIdle()
        }
        if (counterVal < 0) {
            throw IllegalArgumentException("Counter has been corrupted!")
        }
    }
}