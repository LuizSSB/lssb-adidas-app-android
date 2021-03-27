package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class DefaultBaseViewModel<TStateModel, TEffects, TIntent>(defaultValue: TStateModel)
    : ViewModel(), BaseViewModel<TStateModel, TEffects, TIntent> {
    final override val state = MutableLiveData<TStateModel>()

    val stateValue get() = state.value!!

    final override val effects = MutableLiveData<TEffects>()

    init {
        state.value = defaultValue
    }

    private var started = false

    final override fun startOrResume() {
        if (!started) {
            start()
            started = true
        } else {
            // luizssb: triggers update
            state.value = state.value
        }
    }

    protected abstract fun start()
}
