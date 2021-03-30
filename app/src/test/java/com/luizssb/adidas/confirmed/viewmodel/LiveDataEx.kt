package com.luizssb.adidas.confirmed.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

abstract class LiveDataEx private constructor() {
    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.NONE)
        fun <T> LiveData<T>.getOrAwaitValue(
                time: Long = 2,
                timeUnit: TimeUnit = TimeUnit.SECONDS,
                afterObserve: () -> Unit = {}
        ): T {
            var data: T? = null
            val latch = CountDownLatch(1)
            val observer = object : Observer<T> {
                override fun onChanged(t: T) {
                    data = t
                    latch.countDown()
                    removeObserver(this)
                }
            }
            this.observeForever(observer)
            try {
                afterObserve()
                if(!latch.await(time, timeUnit)) {
                    throw TimeoutException("LiveData value was never set.")
                }
            } finally {
                removeObserver(observer)
            }

            return data!!
        }
    }
}
