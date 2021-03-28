package com.luizssb.adidas.confirmed.utils.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowEx private constructor() {
    companion object {
        // luizssb: inspired by
        // https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
        fun <T> Flow<T>.observeOnLifecycle(
                lifecycleOwner: LifecycleOwner,
                observer: suspend (T) -> Unit
        )  {
            var job: Job? = null
            lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
                when (event) {
                    Lifecycle.Event.ON_START ->
                        job = source.lifecycleScope.launch { this@observeOnLifecycle.collect(observer) }
                    Lifecycle.Event.ON_STOP -> {
                        job?.cancel()
                        job = null
                    }
                    else -> {}
                }
            })
        }
    }
}
